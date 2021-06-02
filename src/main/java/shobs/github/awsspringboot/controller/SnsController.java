package shobs.github.awsspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationSubject;
import org.springframework.cloud.aws.messaging.endpoint.NotificationStatus;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationMessageMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import org.springframework.cloud.aws.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shobs.github.awsspringboot.handler.SnsHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/springboot-sns-without-sqs")
public class SnsController {

    @Autowired
    private SnsHandler snsHandler;

    @NotificationSubscriptionMapping
    public void handleSubscriptionMessage(NotificationStatus status) throws IOException {
        //We subscribe to start receive the message
        status.confirmSubscription();
    }

    @NotificationMessageMapping
    public void handleNotificationMessage(@NotificationSubject String subject, @NotificationMessage String message) {
        // ...
    }

    @NotificationUnsubscribeConfirmationMapping
    public void handleUnsubscribeMessage(NotificationStatus status) {
        //e.g. the client has been unsubscribed and we want to "re-subscribe"
        status.confirmSubscription();
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMessageToSns(@RequestBody String message) {
        try {
            log.info("message {}", message);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> requestMap = mapper.readValue(message, Map.class);
            snsHandler.send(requestMap.get("message"), requestMap.get("subject"));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }

    }

}