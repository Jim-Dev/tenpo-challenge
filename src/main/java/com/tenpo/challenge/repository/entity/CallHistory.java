package com.tenpo.challenge.repository.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing an API call history entry.
 */
@Entity
@Table(name = "call_history")
@SuppressWarnings("checkstyle:DesignForExtension")
public class CallHistory {

    /** The unique identifier for the call history entry. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The timestamp when the API call was made. */
    @Column(nullable = false)
    private Instant timestamp;

    /** The API endpoint that was called. */
    @Column(nullable = false)
    private String endpoint;

    /** The parameters passed to the API call. */
    @Column(columnDefinition = "TEXT")
    private String parameters;

    /** The response returned by the API call. */
    @Column(columnDefinition = "TEXT")
    private String response;

    /** The error message if the API call failed. */
    @Column(columnDefinition = "TEXT")
    private String error;

    /**
     * Creates a new call history entry.
     */
    public CallHistory() {
    }

    /**
     * Creates a new call history entry with all fields.
     *
     * @param timestamp the timestamp of the call
     * @param endpoint the API endpoint
     * @param parameters the request parameters
     * @param response the response
     * @param error the error message
     */
    public CallHistory(
            final Instant timestamp,
            final String endpoint,
            final String parameters,
            final String response,
            final String error) {
        this.timestamp = timestamp;
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.response = response;
        this.error = error;
    }

    /**
     * Gets the unique identifier.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the endpoint.
     *
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Gets the parameters.
     *
     * @return the parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Gets the response.
     *
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets the error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(final Instant timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Sets the endpoint.
     *
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Sets the parameters.
     *
     * @param parameters the parameters to set
     */
    public void setParameters(final String parameters) {
        this.parameters = parameters;
    }

    /**
     * Sets the response.
     *
     * @param response the response to set
     */
    public void setResponse(final String response) {
        this.response = response;
    }

    /**
     * Sets the error.
     *
     * @param error the error to set
     */
    public void setError(final String error) {
        this.error = error;
    }
}
