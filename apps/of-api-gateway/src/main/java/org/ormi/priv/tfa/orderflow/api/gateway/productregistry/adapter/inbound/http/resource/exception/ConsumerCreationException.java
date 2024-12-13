package org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.resource.exception;

/**
 * Custom exception thrown when the Pulsar consumer cannot be created.
 */
public class ConsumerCreationException extends RuntimeException {

    /**
     * Constructs a new {@code ConsumerCreationException} with the specified detail message.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public ConsumerCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ConsumerCreationException} with the specified detail message and cause.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     * @param cause the cause of the exception, which is saved for later retrieval by the {@link Throwable#getCause()} method.
     */
    public ConsumerCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}