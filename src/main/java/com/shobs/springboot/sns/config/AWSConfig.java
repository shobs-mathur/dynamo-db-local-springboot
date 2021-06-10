package com.shobs.springboot.sns.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.Getter;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSns
public class AWSConfig {

    @Getter
    private AmazonSNS amazonSNSClient;

    public AmazonSNS initiateClient() {
        amazonSNSClient = AmazonSNSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar")))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "af-south-1"))
                .build();
        return amazonSNSClient;
    }

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate() {
        amazonSNSClient = initiateClient();
        return new NotificationMessagingTemplate(amazonSNSClient);
    }
}
