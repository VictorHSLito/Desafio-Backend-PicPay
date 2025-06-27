package com.victor.picpay.clients.services;

import com.victor.picpay.clients.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationClient notificationClient;

    public void sendNotification() {
        notificationClient.sendNotification();
    }
}
