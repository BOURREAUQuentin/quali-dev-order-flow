package org.ormi.priv.tfa.orderflow.product.registry.exception;

/**
 * Exception thrown when there is a failure during event production.
 * This exception is used when an error occurs while trying to produce a product registry event.
 */
public class EventProductionFailureException extends ProductRegistryEventException {

    /**
     * Constructor with a custom message.
     * Creates a new instance of EventProductionFailureException with a specified message.
     *
     * @param message the detail message explaining the exception
     */
    public EventProductionFailureException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and cause.
     * Creates a new instance of EventProductionFailureException with a specified message and cause.
     *
     * @param message the detail message explaining the exception
     * @param cause the cause of the exception (can be null)
     */
    public EventProductionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}