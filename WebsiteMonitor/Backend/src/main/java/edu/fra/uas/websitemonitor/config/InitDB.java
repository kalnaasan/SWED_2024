package edu.fra.uas.websitemonitor.config;

import edu.fra.uas.websitemonitor.model.CommunicationChannel;
import edu.fra.uas.websitemonitor.model.Frequency;
import edu.fra.uas.websitemonitor.model.Subscription;
import edu.fra.uas.websitemonitor.model.User;
import edu.fra.uas.websitemonitor.repository.SubscriptionRepository;
import edu.fra.uas.websitemonitor.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@Slf4j
public class InitDB {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public InitDB(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostConstruct
    private void init() {
        log.info("### Start initialization of DB ###");
        User kalnaasan = this.createUser("Kaddour", "Alnaasan", "kaddour@alnaasan.de", "017622930808");
        User mdawoud = this.createUser("Mohammed", "Dawoud", "qaduralnaasan@gmail.com", "0176229308080");
        Subscription subscriptionWikipedia = this.createSubscription("Wikipedia", "https://de.wikipedia.org/wiki/Wikipedia:Hauptseite", Frequency.HOURLY, CommunicationChannel.EMAIL, kalnaasan);
        Subscription subscriptionSpiegel = this.createSubscription("Der spiegel", "https://www.spiegel.de/", Frequency.HOURLY, CommunicationChannel.EMAIL, kalnaasan);
        Subscription subscriptionErste = this.createSubscription("Das Erste", "https://www.daserste.de/", Frequency.HOURLY, CommunicationChannel.EMAIL, kalnaasan);
        Subscription subscriptionZDF = this.createSubscription("ZDF", "https://www.zdf.de/", Frequency.HOURLY, CommunicationChannel.EMAIL, kalnaasan);
        Subscription subscriptionFAZ = this.createSubscription("FAZ", "https://www.faz.net/aktuell/", Frequency.HOURLY, CommunicationChannel.EMAIL, mdawoud);
    }

    private User createUser(String firstName, String lastName, String email, String phone) {
        return this.userRepository.findByEmail(email)
                .orElseGet(() -> this.userRepository.save(new User(null, firstName, lastName, email, phone, new HashSet<>())));
    }

    private Subscription createSubscription(String websiteName, String url, Frequency frequency, CommunicationChannel communicationChannel, User user) {
        return this.subscriptionRepository.findByWebsiteNameAndUrl(websiteName, url)
                .orElseGet(() -> this.subscriptionRepository.save(
                        new Subscription(null, websiteName, url, frequency, communicationChannel, null, user, new HashSet<>())));
    }
}
