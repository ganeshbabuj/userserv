package com.example.messaging.dispatcher;

import com.example.vo.Notification;

public interface NotificationDispatcher {

    public void dispatch(Notification notification);
}
