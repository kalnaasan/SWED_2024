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
    private final EmailNotification emailNotification;
    private final SubscriptionRepository subscriptionRepository;
    private final VersionRepository versionRepository;

    @Autowired
    public SubscriptionService(EmailNotification emailNotification, SubscriptionRepository subscriptionRepository, VersionRepository versionRepository) {
        this.emailNotification = emailNotification;
        this.subscriptionRepository = subscriptionRepository;
        this.versionRepository = versionRepository;
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

    @Scheduled(fixedRate = 5000) // 5000 milliseconds = 5 seconds
    public void run() {
        log.info("run");
        this.subscriptionRepository.getAllIDs().forEach(subscriptionId -> {
            try {
                Boolean compare = this.fetchAndCompareWebsiteContent(subscriptionId);
                if (!compare) emailNotification.sendNotification(getSubscriptionById(subscriptionId).get());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Boolean fetchAndCompareWebsiteContent(Long subscriptionId) throws IOException {
        // Find the subscription by ID
        Optional<Subscription> optionalSubscription = this.subscriptionRepository.findById(subscriptionId);

        // Check if the subscription is present
        if (optionalSubscription.isEmpty()) {
            // If the subscription is not found, return false
            return false;
        }

        // Get the subscription from the Optional
        Subscription subscription = optionalSubscription.get();
        // Get the frequency of the subscription
        Frequency frequency = subscription.getFrequency();
        // Get the current time
        LocalDateTime now = LocalDateTime.now();

        // Fetch the most recent version of the content for this subscription
        List<Version> recentVersions = this.versionRepository.findTop2BySubscription_IdOrderByCreatedAtDesc(subscriptionId);

        // Check if there are any previously saved versions
        if (!recentVersions.isEmpty()) {
            // Get the most recent version
            Version lastVersion = recentVersions.get(0);
            // Get the time when the most recent version was saved
            LocalDateTime lastSavedTime = lastVersion.getCreatedAt();
            // Calculate the next eligible save time based on the frequency
            LocalDateTime nextSaveTime = this.calculateNextSaveTime(lastSavedTime, frequency);

            // If the current time is before the next eligible save time, return false
            if (now.isBefore(nextSaveTime)) {
                return false;
            }
        }

        // Fetch the content from the URL
        String content = this.fetchContent(subscription.getUrl());
        // Save the new version of the content
        this.versionRepository.save(new Version(null, content, now, subscription));

        // Check if there are at least two versions to compare
        if (recentVersions.size() < 2) {
            // If not enough versions, return false
            return false;
        }

        // Get the most recent version
        Version version1 = recentVersions.get(0);
        // Get the second most recent version
        Version version2 = recentVersions.get(1);

        // Compare the content of the two most recent versions
        return version1.getContent().equals(version2.getContent());
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
