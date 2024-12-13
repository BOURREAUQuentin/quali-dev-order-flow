package org.ormi.priv.tfa.orderflow.product.registry.exception;

/**
 * Exception thrown when there is a failure while closing the event producer.
 * This exception is used when an error occurs during the process of closing a producer instance.
 */
public class ProducerCloseFailureException extends ProductRegistryEventException {

    /**
     * Constructor with a custom message.
     * Creates a new instance of ProducerCloseFailureException with a specified message.
     *
     * @param message the detail message explaining the exception
     */
    public ProducerCloseFailureException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and cause.
     * Creates a new instance of ProducerCloseFailureException with a specified message and cause.
     *
     * @param message the detail message explaining the exception
     * @param cause the cause of the exception (can be null)
     */
    public ProducerCloseFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}