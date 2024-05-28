package edu.fra.uas.websitemonitor.controller;


import edu.fra.uas.websitemonitor.model.Subscription;
import edu.fra.uas.websitemonitor.response.MessageResponse;
import edu.fra.uas.websitemonitor.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing subscriptions.
 */
@RestController
@RequestMapping(value = "/api/v1/subscriptions")
@Slf4j
public class SubscriptionController {

    // User service to handle business logic
    private final SubscriptionService subscriptionService;

    /**
     * Constructor for SubscriptionController.
     *
     * @param subscriptionService the subscription service
     */
    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        // Initialize the subscriptionService
        this.subscriptionService = subscriptionService;
    }

    /**
     * GET / : Get all subscriptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subscriptions
     */
    @GetMapping
    public ResponseEntity<MessageResponse> getAllSubscriptions() {
        // Log the request to get all subscriptions
        log.info("REST request to get all subscriptions");
        // Build and return the response with the list of all subscriptions
        return this.buildResponse("get all subscriptions", this.subscriptionService.getAllSubscriptions(), HttpStatus.OK);
    }

    /**
     * GET /{id} : Get subscription by ID.
     *
     * @param id the ID of the subscription to retrieve
     * @return the ResponseEntity with status 200 (OK) and the subscription, or status 404 (Not Found) if the subscription is not found
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> getSubscriptionById(@PathVariable("id") Long id) {
        // Log the request to get a subscription by ID
        log.info("REST request to get subscription by id; {}", id);
        // Get the subscription by ID and build the response
        return this.subscriptionService.getSubscriptionById(id).map(
                        subscription -> this.buildResponse("get subscription by id", subscription, HttpStatus.OK))
                .orElseGet(() -> this.buildResponse("Subscription cannot be found with id -> " + id, null, HttpStatus.NOT_FOUND));
    }

    /**
     * POST / : Create a new subscription.
     *
     * @param subscription the subscription to create
     * @return the ResponseEntity with status 201 (Created) and the created subscription
     */
    @PostMapping
    public ResponseEntity<MessageResponse> createSubscription(@RequestBody Subscription subscription) {
        // Log the request to create a subscription
        log.debug("REST request to create subscription");
        // Create the subscription and build the response
        return this.buildResponse("subscription is created", this.subscriptionService.createSubscription(subscription), HttpStatus.CREATED);
    }

    /**
     * PUT / : Update an existing subscription.
     *
     * @param subscription the subscription to update
     * @return the ResponseEntity with status 202 (Accepted) and the updated subscription
     */
    @PutMapping
    public ResponseEntity<MessageResponse> updateSubscription(@RequestBody Subscription subscription) {
        // Log the request to update a subscription
        log.debug("REST request to update subscription");
        // Update the subscription and build the response
        return this.buildResponse("subscription is updated", this.subscriptionService.updateSubscription(subscription), HttpStatus.ACCEPTED);
    }

    /**
     * DELETE /{id} : Delete a subscription by ID.
     *
     * @param id the ID of the subscription to delete
     * @return the ResponseEntity with status 202 (Accepted)
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> deleteSubscription(@PathVariable("id") Long id) {
        // Log the request to delete a subscription
        log.debug("REST request to delete subscription");
        // Delete the subscription
        this.subscriptionService.deleteSubscription(id);
        // Build and return the response indicating the subscription is deleted
        return this.buildResponse("subscription is deleted", null, HttpStatus.ACCEPTED);
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
