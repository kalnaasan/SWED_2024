package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.model.Subscription;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("SMSNotification")
public class SMSNotification implements Notification {

    @Override
    public void sendNotification(Subscription subscription) {
    }

}
