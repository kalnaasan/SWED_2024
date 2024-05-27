package edu.fra.uas.websitemonitor.repository;

import edu.fra.uas.websitemonitor.model.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {
    List<Version> findTop2ByOrderByCreatedAtDesc();
    List<Version> findTop2BySubscription_IdOrderByCreatedAtDesc(Long subscriptionId);

    List<Version> findTop1BySubscription_IdOrderByCreatedAtDesc(Long subscriptionId);
}
