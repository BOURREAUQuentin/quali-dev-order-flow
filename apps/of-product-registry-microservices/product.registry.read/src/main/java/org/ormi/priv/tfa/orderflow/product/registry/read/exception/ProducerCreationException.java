package org.ormi.priv.tfa.orderflow.product.registry.read.exception;

/**
 * Custom exception thrown when a Pulsar producer cannot be created.
 */
public class ProducerCreationException extends RuntimeException {

    /**
     * Constructs a new {@code ProducerCreationException} with the specified detail message.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public ProducerCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProducerCreationException} with the specified detail message and cause.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     * @param cause the cause of the exception, which is saved for later retrieval by the {@link Throwable#getCause()} method.
     */
    public ProducerCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
