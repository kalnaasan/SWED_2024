package edu.fra.uas.websitemonitor.controller;


import edu.fra.uas.websitemonitor.model.User;
import edu.fra.uas.websitemonitor.response.MessageResponse;
import edu.fra.uas.websitemonitor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping(value = "/api/v1/users")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    // User service to handle business logic
    private final UserService userService;

    /**
     * Constructor for UserController.
     *
     * @param userService the user service
     */
    @Autowired
    public UserController(UserService userService) {
        // Initialize the userService
        this.userService = userService;
    }

    /**
     * GET / : Get all users.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users
     */
    @GetMapping
    public ResponseEntity<MessageResponse> getAllUsers() {
        // Log the request to get all users
        log.info("REST request to get all users");
        // Build and return the response with the list of all users
        return this.buildResponse("get all users", this.userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * GET /{id} : Get user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and the user, or status 404 (Not Found) if the user is not found
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> getUserById(@PathVariable("id") Long id) {
        // Log the request to get a user by ID
        log.info("REST request to get user by id; {}", id);
        // Get the user by ID and build the response
        return this.userService.getUserById(id).map(
                        user -> this.buildResponse("get user by id", user, HttpStatus.OK))
                .orElseGet(() -> this.buildResponse("User cannot be found with id -> " + id, null, HttpStatus.NOT_FOUND));
    }

    /**
     * POST / : Create a new user.
     *
     * @param user the user to create
     * @return the ResponseEntity with status 201 (Created) and the created user
     */
    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@RequestBody User user) {
        // Log the request to create a user
        log.debug("REST request to create user");
        // Create the user and build the response
        return this.buildResponse("user is created", this.userService.createUser(user), HttpStatus.CREATED);
    }

    /**
     * PUT / : Update an existing user.
     *
     * @param user the user to update
     * @return the ResponseEntity with status 202 (Accepted) and the updated user
     */
    @PutMapping
    public ResponseEntity<MessageResponse> updateUser(@RequestBody User user) {
        // Log the request to update a user
        log.debug("REST request to update user");
        // Update the user and build the response
        return this.buildResponse("user is updated", this.userService.updateUser(user), HttpStatus.ACCEPTED);
    }

    /**
     * DELETE /{id} : Delete a user by ID.
     *
     * @param id the ID of the user to delete
     * @return the ResponseEntity with status 202 (Accepted)
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("id") Long id) {
        // Log the request to delete a user
        log.debug("REST request to delete user");
        // Delete the user
        this.userService.deleteUser(id);
        // Build and return the response indicating the user is deleted
        return this.buildResponse("user is deleted", null, HttpStatus.ACCEPTED);
    }

    /**
     * Helper method to build a response.
     *
     * @param message    the message to include in the response
     * @param data       the data to include in the response
     * @param httpStatus the HTTP status to return
     * @return the ResponseEntity containing the message and data
     */
    private ResponseEntity<MessageResponse> buildResponse(String message, Object data, HttpStatus httpStatus) {
        // Build and return the response entity with the message, data, and status
        return new ResponseEntity<>(new MessageResponse(message, data), httpStatus);
    }
}
