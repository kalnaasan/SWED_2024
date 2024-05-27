package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.model.Subscription;

public interface Notification {
    void sendNotification(Subscription subscription);
}
