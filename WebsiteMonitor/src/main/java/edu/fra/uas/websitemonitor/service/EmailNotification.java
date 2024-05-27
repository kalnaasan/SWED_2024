package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@Qualifier("EmailNotification")
public class EmailNotification implements Notification {

    private final EmailService emailService;

    @Autowired
    public EmailNotification(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendNotification(Subscription subscription) {
        String name = subscription.getUser().getFullName();
        String email = subscription.getUser().getEmail();
        String website = subscription.getWebsiteName();
        String url = subscription.getUrl();
        String body = String.format("""
                Dear Ms/Mr %s,
                                    
                There is an update on the Page '%s'. Please click on the link below:
                %s
                                    
                Thanks
                """, name, website, url);
        this.emailService.sendSimpleMessage(email, "New Update", body);
    }

}
