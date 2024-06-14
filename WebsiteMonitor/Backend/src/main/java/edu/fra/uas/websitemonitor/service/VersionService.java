package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.exception.ResourceNotFoundException;
import edu.fra.uas.websitemonitor.model.Version;
import edu.fra.uas.websitemonitor.observer.EmailObserver;
import edu.fra.uas.websitemonitor.repository.VersionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing versions.
 * This class provides methods to perform CRUD operations on versions.
 */
@Service
@Slf4j
public class VersionService {
    private final VersionRepository versionRepository;
    private final EmailObserver emailObserver;

    /**
     * Constructor for VersionService.
     *
     * @param versionRepository the VersionRepository to interact with the database
     */
    @Autowired
    public VersionService(VersionRepository versionRepository, EmailObserver emailObserver) {
        this.versionRepository = versionRepository;
        this.emailObserver = emailObserver;
    }

    /**
     * Creates a new version.
     *
     * @param version the version to be created
     * @return the created version
     */
    public Version createVersion(Version version) {
        version.attach(emailObserver);
        return this.versionRepository.save(version);
    }

    /**
     * Retrieves a version by their ID.
     *
     * @param id the ID of the version to be retrieved
     * @return an Optional containing the found version, or empty if no version was found
     */
    public Optional<Version> getVersionById(Long id) {
        return this.versionRepository.findById(id);
    }

    /**
     * Retrieves all versions.
     *
     * @return a list of all versions
     */
    public List<Version> getAllVersions() {
        return this.versionRepository.findAll();
    }

    /**
     * Updates an existing version.
     *
     * @param versionDetails the new details for the version
     * @return the updated version
     * @throws ResourceNotFoundException if no version was found with the given ID
     */
    public Version updateVersion(Version versionDetails) {
        return this.versionRepository.findById(versionDetails.getId()).map(version -> {
            version.setContent(versionDetails.getContent());
            version.setCreatedAt(versionDetails.getCreatedAt());
            if (isContentChanged(version)) {
                version.notifyObservers();
            }
            return this.versionRepository.save(version);
        }).orElseThrow(() -> new ResourceNotFoundException("Version not found with id " + versionDetails.getId()));
    }

    /**
     * Deletes a version by their ID.
     *
     * @param id the ID of the version to be deleted
     */
    public void deleteVersion(Long id) {
        this.versionRepository.deleteById(id);
    }

    public boolean isContentChanged(Version newVersion) {
        List<Version> recentVersions = this.versionRepository.findTop2BySubscription_IdOrderByCreatedAtDesc(newVersion.getSubscription().getId());
        if (recentVersions.size() < 2) {
            return false;
        }
        Version lastVersion = recentVersions.get(0);
        Version secondLastVersion = recentVersions.get(1);
        return !lastVersion.getContent().equals(secondLastVersion.getContent());
    }

    public void compareWebsiteContent(Long subscriptionId, Version version) {
        version = this.createVersion(version);

        List<Version> recentVersions = this.versionRepository.findTop2BySubscription_IdOrderByCreatedAtDesc(subscriptionId);

        if (recentVersions.size() >= 2) {
            Version lastVersion = recentVersions.get(0);
            Version secondLastVersion = recentVersions.get(1);

            boolean isContentChanged = !lastVersion.getContent().equals(secondLastVersion.getContent());
            if (isContentChanged) {
                version.notifyObservers();
            }
        }
    }
}