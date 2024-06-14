package edu.fra.uas.websitemonitor.observer;

import edu.fra.uas.websitemonitor.model.Subscription;
import edu.fra.uas.websitemonitor.model.User;
import edu.fra.uas.websitemonitor.model.Version;
import edu.fra.uas.websitemonitor.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailObserver implements Observer {
    private final EmailService emailService;

    @Autowired
    public EmailObserver(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void update(Version version) {
        Subscription subscription = version.getSubscription();
        User user = subscription.getUser();
        String email = user.getEmail();
        String subject = "Subscription Updated";
        String body = "Your subscription has updated: " + subscription.getUrl();
        this.emailService.sendSimpleMessage(email, subject, body);
    }
}
