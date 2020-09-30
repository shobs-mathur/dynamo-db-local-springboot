package shobs.github.dynamodblocalspringboot.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "international-transactions-toggle")
public class InternationalTransactionsToggle {

    @DynamoDBHashKey(attributeName = "clientEncodedKey")
    private String clientEncodedKey;

    @DynamoDBAttribute(attributeName = "accountId")
    private String accountId;

    @DynamoDBAttribute(attributeName = "toggle")
    private Boolean toggle;

    @DynamoDBAttribute(attributeName = "toggleType")
    private Boolean toggleType;

    @DynamoDBAttribute(attributeName = "createDate")
    private Date createDate;

    @DynamoDBAttribute(attributeName = "modifiedDate")
    private Date modifiedDate;
}