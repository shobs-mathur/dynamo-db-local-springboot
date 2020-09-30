package shobs.github.dynamodblocalspringboot.handler;

import com.amazonaws.services.dynamodbv2.model.*;
import shobs.github.dynamodblocalspringboot.config.DynamoDbConfig;
import shobs.github.dynamodblocalspringboot.domain.InternationalTransactionsToggle;
import shobs.github.dynamodblocalspringboot.exception.CustomerControlsException;
import shobs.github.dynamodblocalspringboot.exception.CustomerControlsExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class DynamoDbHandler {

    private DynamoDbConfig dynamoDbConfig;

    @Autowired
    public DynamoDbHandler(DynamoDbConfig dynamoDbConfig) {
        this.dynamoDbConfig = dynamoDbConfig;
    }

    public InternationalTransactionsToggle getToggle(String clientEncodedKey) {
        log.info("getToggle started for client {}", clientEncodedKey);
        try {
            return dynamoDbConfig.dynamoDBMapper().load(InternationalTransactionsToggle.class, clientEncodedKey);
        } catch(Exception e) {
            log.error("exception in getToggle {}", e.getMessage());
            throw new CustomerControlsException(CustomerControlsExceptionCode.CC_1001,
                    MessageFormat.format("getToggle failed with exception {0}", e.getMessage()));
        }
    }

    public void createToggle(String clientEncodedKey) {
        log.info("createToggle started for client {}", clientEncodedKey);
        try {
            dynamoDbConfig.dynamoDBMapper()
                    .save(InternationalTransactionsToggle.builder()
                            .clientEncodedKey(clientEncodedKey)
                            .toggle(true)
                            .createDate(Date.from(Instant.now()))
                            .modifiedDate(null)
                            .build());
            consumeStream();
            log.info("createToggle for client {}", clientEncodedKey);
        } catch(Exception e) {
            log.error("exception in createToggle {}", e.getMessage());
            throw new CustomerControlsException(CustomerControlsExceptionCode.CC_1001,
                    MessageFormat.format("createToggle failed with exception {0}", e.getMessage()));
        }

    }

    public void updateToggle(String clientEncodedKey, boolean toggle) {
        log.info("updateToggle started for client {}", clientEncodedKey);
        try {
            dynamoDbConfig.dynamoDBMapper()
                    .save(InternationalTransactionsToggle.builder()
                            .clientEncodedKey(clientEncodedKey)
                            .toggle(toggle)
                            .createDate(null)
                            .modifiedDate(Date.from(Instant.now()))
                            .build());
            log.info("updateToggle for client {}", clientEncodedKey);
        } catch(Exception e) {
            log.error("exception in updateToggle {}", e.getMessage());
            throw new CustomerControlsException(CustomerControlsExceptionCode.CC_1001,
                    MessageFormat.format("updateToggle failed with exception {0}", e.getMessage()));
        }
    }

    public void consumeStream() {
        log.info("consumeStream");
        DescribeTableResult describeTableResult = dynamoDbConfig.getDynamoDBClient().describeTable("international-transactions-toggle");
        String streamArn = describeTableResult.getTable().getLatestStreamArn();
        log.info("streamArn {}", streamArn);
        DescribeStreamResult describeStreamResult = dynamoDbConfig.dynamoDBStreams()
                .describeStream(new DescribeStreamRequest()
                        .withStreamArn(streamArn)
                        .withExclusiveStartShardId(null));
        List<Shard> shards = describeStreamResult.getStreamDescription().getShards();
        log.info("shards {}", shards.size());

        for (Shard shard : shards) {
            String shardId = shard.getShardId();
            System.out.println("Shard: " + shard);

            GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest()
                    .withStreamArn(streamArn)
                    .withShardId(shardId)
                    .withShardIteratorType(ShardIteratorType.TRIM_HORIZON);
            GetShardIteratorResult getShardIteratorResult = dynamoDbConfig.dynamoDBStreams().getShardIterator(getShardIteratorRequest);
            String currentShardIter = getShardIteratorResult.getShardIterator();

            int processedRecordCount = 0;
            while (currentShardIter != null && processedRecordCount < 5) {
                GetRecordsResult getRecordsResult = dynamoDbConfig.dynamoDBStreams().getRecords(new GetRecordsRequest()
                        .withShardIterator(currentShardIter));
                List<Record> records = getRecordsResult.getRecords();
                for (Record record : records) {
                    System.out.println("        " + record.getDynamodb());
                }
                processedRecordCount += records.size();
                currentShardIter = getRecordsResult.getNextShardIterator();
            }
        }
    }
}
