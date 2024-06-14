package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.exception.ResourceNotFoundException;
import edu.fra.uas.websitemonitor.model.Frequency;
import edu.fra.uas.websitemonitor.model.Subscription;
import edu.fra.uas.websitemonitor.model.Version;
import edu.fra.uas.websitemonitor.repository.SubscriptionRepository;
import edu.fra.uas.websitemonitor.repository.VersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionService {

    @Qualifier("EmailNotification")
    private final Notification emailNotification;
    @Qualifier("SMSNotification")
    private final Notification smsNotification;
    private final SubscriptionRepository subscriptionRepository;
    private final VersionRepository versionRepository;
    private final VersionService versionService;

    @Autowired
    public SubscriptionService(@Qualifier("EmailNotification") Notification emailNotification,
                               @Qualifier("SMSNotification") Notification smsNotification,
                               SubscriptionRepository subscriptionRepository, VersionRepository versionRepository, VersionService versionService) {
        this.emailNotification = emailNotification;
        this.smsNotification = smsNotification;
        this.subscriptionRepository = subscriptionRepository;
        this.versionRepository = versionRepository;
        this.versionService = versionService;
    }

    /**
     * Creates a new subscription.
     *
     * @param subscription the subscription to be created
     * @return the created subscription
     */
    public Subscription createSubscription(Subscription subscription) {
        return this.subscriptionRepository.save(subscription);
    }

    /**
     * Retrieves a subscription by their ID.
     *
     * @param id the ID of the subscription to be retrieved
     * @return an Optional containing the found subscription, or empty if no subscription was found
     */
    public Optional<Subscription> getSubscriptionById(Long id) {
        return this.subscriptionRepository.findById(id);
    }

    /**
     * Retrieves all subscriptions.
     *
     * @return a list of all subscriptions
     */
    public List<Subscription> getAllSubscriptions() {
        return this.subscriptionRepository.findAll();
    }

    /**
     * Updates an existing subscription.
     *
     * @param subscriptionDetails the new details for the subscription
     * @return the updated subscription
     * @throws ResourceNotFoundException if no subscription was found with the given ID
     */
    public Subscription updateSubscription(Subscription subscriptionDetails) {
        return this.subscriptionRepository.findById(subscriptionDetails.getId()).map(subscription -> {
            subscription.setWebsiteName(subscriptionDetails.getWebsiteName());
            subscription.setUrl(subscriptionDetails.getUrl());
            subscription.setFrequency(subscriptionDetails.getFrequency());
            subscription.setCommunicationChannel(subscriptionDetails.getCommunicationChannel());
            return this.subscriptionRepository.save(subscription);
        }).orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + subscriptionDetails.getId()));
    }

    /**
     * Deletes a subscription by their ID.
     *
     * @param id the ID of the subscription to be deleted
     */
    public void deleteSubscription(Long id) {
        this.subscriptionRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 60000) // 60000 milliseconds = 1 minute
    public void run() {
        log.info("run");
        this.subscriptionRepository.getAllIDs().forEach(subscriptionId -> {
            try {
                fetchAndCompareWebsiteContent(subscriptionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void fetchAndCompareWebsiteContent(Long subscriptionId) throws IOException {
        Optional<Subscription> optionalSubscription = this.subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isEmpty()) {
            throw new ResourceNotFoundException("Subscription not found with id " + subscriptionId);
        }

        Subscription subscription = optionalSubscription.get();
        Frequency frequency = subscription.getFrequency();
        LocalDateTime now = LocalDateTime.now();

        log.info("Subscription: {} , Frequency: {}", subscription.getWebsiteName(), frequency.toString());

        List<Version> recentVersions = this.versionRepository.findTop2BySubscription_IdOrderByCreatedAtDesc(subscriptionId);

        if (!recentVersions.isEmpty()) {
            Version lastVersion = recentVersions.get(0);
            LocalDateTime lastSavedTime = lastVersion.getCreatedAt();
            LocalDateTime nextSaveTime = calculateNextSaveTime(lastSavedTime, frequency);

            if (now.isBefore(nextSaveTime)) {
                return;
            }
        }

        String content = this.fetchContent(subscription.getUrl());
        Version version = new Version(content, now, subscription);
        this.versionService.compareWebsiteContent(subscriptionId, version);
    }

    /**
     * Downloads the source code of a webpage.
     *
     * @param webpageURI the URL of the webpage to download
     * @return the source code of the webpage as a String
     * @throws IOException if an I/O exception occurs
     */
    private String fetchContent(String webpageURI) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(webpageURI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        }

        return result.toString();
    }

    private LocalDateTime calculateNextSaveTime(LocalDateTime lastSavedTime, Frequency frequency) {
        // Calculate the next save time based on the frequency
        return switch (frequency) {
            case MINUTELY -> lastSavedTime.plusMinutes(1);
            case HOURLY -> lastSavedTime.plusHours(1);
            case WEEKLY -> lastSavedTime.plusWeeks(1);
            case MONTHLY -> lastSavedTime.plusMonths(1);
            default -> LocalDateTime.MIN; // NONE or unsupported frequencies mean no saving
        };
    }
}
