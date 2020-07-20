package com.example.messaging.dispatcher;

import com.example.messaging.NotificationLogBook;
import com.example.vo.Notification;

public class SMSNotificationDispatcher implements NotificationDispatcher {

    @Override
    public void dispatch(Notification notification) {

        try {
            // intentional to demonstrate asynchronous message processing
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            //ignore
        }

        System.out.println("\n** NOTIFICATION | " + notification.getRecipient() + " | DISPATCHED | " + notification + " **\n");

        // Just for demonstration. Avoid using singleton. instead use Spring bean
        // Get NotificationLogBook instance
        NotificationLogBook notificationLogBook = NotificationLogBook.getInstance();
        notificationLogBook.addEntry(notification);

    }
}
