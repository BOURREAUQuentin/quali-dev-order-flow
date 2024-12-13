package org.ormi.priv.tfa.orderflow.product.registry.exception;

/**
 * Custom exception for errors related to product registry events.
 * This exception serves as a base class for other specific exceptions related to event production and handling.
 */
public class ProductRegistryEventException extends RuntimeException {

    /**
     * Default constructor.
     * Creates a new instance of ProductRegistryEventException without a message or cause.
     */
    public ProductRegistryEventException() {
        super();
    }

    /**
     * Constructor with a custom message.
     * Creates a new instance of ProductRegistryEventException with a specified message.
     *
     * @param message the detail message explaining the exception
     */
    public ProductRegistryEventException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and cause.
     * Creates a new instance of ProductRegistryEventException with a specified message and cause.
     *
     * @param message the detail message explaining the exception
     * @param cause the cause of the exception (can be null)
     */
    public ProductRegistryEventException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     * Creates a new instance of ProductRegistryEventException with the specified cause and a default message.
     *
     * @param cause the cause of the exception (can be null)
     */
    public ProductRegistryEventException(Throwable cause) {
        super(cause);
    }
}
