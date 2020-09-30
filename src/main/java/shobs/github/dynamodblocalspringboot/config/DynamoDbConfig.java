package shobs.github.dynamodblocalspringboot.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.*;
import shobs.github.dynamodblocalspringboot.domain.InternationalTransactionsToggle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@Slf4j
public class DynamoDbConfig {

    @Getter
    private AmazonDynamoDB dynamoDBClient;

    @Bean("dynamodb")
    public DynamoDBMapper dynamoDBMapper() {
        dynamoDBClient = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new ProfileCredentialsProvider("codehub"))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", "af-south-1"))
                .build();
        DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient, DynamoDBMapperConfig.DEFAULT);
        initDynamoDb(mapper, dynamoDBClient);
        return mapper;
    }

    void initDynamoDb(DynamoDBMapper mapper, AmazonDynamoDB dynamoDB) {
        log.info("initDynamoDb");
        try {
            CreateTableRequest req = mapper.generateCreateTableRequest(InternationalTransactionsToggle.class);
            req.setStreamSpecification(new StreamSpecification()
                    .withStreamEnabled(true)
                    .withStreamViewType(StreamViewType.NEW_AND_OLD_IMAGES));
            req.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            dynamoDB.createTable(req);
        } catch (ResourceInUseException e) {
            log.error("initDynamoDb failed; table already exists");
        }
    }

    @Bean
    @DependsOn("dynamodb")
    public AmazonDynamoDBStreams dynamoDBStreams() {
        return AmazonDynamoDBStreamsClientBuilder
                .standard()
                .withCredentials(new ProfileCredentialsProvider("codehub"))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", "af-south-1"))
                .build();
    }
}