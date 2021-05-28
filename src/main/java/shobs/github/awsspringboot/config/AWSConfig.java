package shobs.github.awsspringboot.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSns
@EnableSqs
public class AWSConfig {

    @Getter
    private AmazonSNS amazonSNSClient;


    @Setter
    @Value("${cloud.aws.region.static:af-south-1}")
    private String region;

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate() {
        amazonSNSClient = AmazonSNSClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider("localstack"))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "af-south-1"))
                .build();
        return new NotificationMessagingTemplate(amazonSNSClient);
    }
}
