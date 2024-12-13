package org.ormi.priv.tfa.orderflow.product.registry.read.exception;

/**
 * Custom exception thrown when a message cannot be sent through a Pulsar producer.
 */
public class MessageSendingException extends RuntimeException {

    /**
     * Constructs a new {@code MessageSendingException} with the specified detail message.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public MessageSendingException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code MessageSendingException} with the specified detail message and cause.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     * @param cause the cause of the exception, which is saved for later retrieval by the {@link Throwable#getCause()} method.
     */
    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
