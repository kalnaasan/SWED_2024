package edu.fra.uas.websitemonitor.service;

import edu.fra.uas.websitemonitor.exception.ResourceNotFoundException;
import edu.fra.uas.websitemonitor.model.User;
import edu.fra.uas.websitemonitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users.
 * This class provides methods to perform CRUD operations on users.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository the UserRepository to interact with the database
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     *
     * @param user the user to be created
     * @return the created user
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to be retrieved
     * @return an Optional containing the found user, or empty if no user was found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates an existing user.
     *
     * @param userDetails the new details for the user
     * @return the updated user
     * @throws ResourceNotFoundException if no user was found with the given ID
     */
    public User updateUser(User userDetails) {
        return userRepository.findById(userDetails.getId()).map(user -> {
            user.setFirstname(userDetails.getFirstname());
            user.setLastname(userDetails.getLastname());
            user.setEmail(userDetails.getEmail());
            user.setPhone(userDetails.getPhone());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userDetails.getId()));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
