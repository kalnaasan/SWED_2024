package edu.fra.uas.websitemonitor.repository;

import edu.fra.uas.websitemonitor.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByWebsiteNameAndUrl(String websiteName, String url);

    @Query("SELECT s.id FROM Subscription s")
    List<Long> getAllIDs();
}
