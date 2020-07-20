package com.example.messaging.listener;

import com.example.messaging.dispatcher.NotificationDispatcher;
import com.example.messaging.dispatcher.NotificationDispatcherFactory;
import com.example.vo.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    NotificationDispatcherFactory notificationDispatcherFactory;

    @JmsListener(destination = "notificationQueue", containerFactory = "myFactory")
    public void receiveMessage(Notification notification) {

        System.out.println("\n** NOTIFICATION | " + notification.getRecipient() + " | RECEIVED | " + notification + " **\n");

        NotificationDispatcher notificationDispatcher = notificationDispatcherFactory.createNotificationDispatcher(notification.getType());
        notificationDispatcher.dispatch(notification);

    }

}
