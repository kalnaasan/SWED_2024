package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("SMSNotification")
@Slf4j
public class SMSNotification implements Notification {

    @Override
    public void sendNotification(Subscription subscription) {
      log.info("SMS sending to {}", subscription.getUser().getPhone());
    }

}
