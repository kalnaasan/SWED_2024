package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.exception.ResourceNotFoundException;
import edu.fra.uas.websitemonitor.model.Version;
import edu.fra.uas.websitemonitor.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing versions.
 * This class provides methods to perform CRUD operations on versions.
 */
@Service
public class VersionService {
    private final VersionRepository versionRepository;

    /**
     * Constructor for VersionService.
     *
     * @param versionRepository the VersionRepository to interact with the database
     */
    @Autowired
    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    /**
     * Creates a new version.
     *
     * @param version the version to be created
     * @return the created version
     */
    public Version createVersion(Version version) {
        return versionRepository.save(version);
    }

    /**
     * Retrieves a version by their ID.
     *
     * @param id the ID of the version to be retrieved
     * @return an Optional containing the found version, or empty if no version was found
     */
    public Optional<Version> getVersionById(Long id) {
        return versionRepository.findById(id);
    }

    /**
     * Retrieves all versions.
     *
     * @return a list of all versions
     */
    public List<Version> getAllVersions() {
        return versionRepository.findAll();
    }

    /**
     * Updates an existing version.
     *
     * @param versionDetails the new details for the version
     * @return the updated version
     * @throws ResourceNotFoundException if no version was found with the given ID
     */
    public Version updateVersion(Version versionDetails) {
        return versionRepository.findById(versionDetails.getId()).map(version -> {
            version.setContent(versionDetails.getContent());
            version.setCreatedAt(versionDetails.getCreatedAt());
            return versionRepository.save(version);
        }).orElseThrow(() -> new ResourceNotFoundException("Version not found with id " + versionDetails.getId()));
    }

    /**
     * Deletes a version by their ID.
     *
     * @param id the ID of the version to be deleted
     */
    public void deleteVersion(Long id) {
        versionRepository.deleteById(id);
    }
}
