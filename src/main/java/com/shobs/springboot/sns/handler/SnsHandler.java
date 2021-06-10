package com.shobs.springboot.sns.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SnsHandler {

    @Autowired
    NotificationMessagingTemplate messagingTemplate;

    public void send(String message, String subject) {
        messagingTemplate
                .sendNotification("springboot-sns-without-sqs", message, subject);
    }
}
