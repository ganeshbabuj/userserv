package com.example.messaging;

import com.example.vo.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {

    @Autowired
    JmsTemplate jmsTemplate;

    public void send(Notification notification) {

        // Send a message with a POJO - the template reuse the message converter
        System.out.println("\n** NOTIFICATION | " + notification.getRecipient() + " | SENT TO QUEUE | " + notification + " **\n");
        jmsTemplate.convertAndSend("notificationQueue", notification);
    }
}
