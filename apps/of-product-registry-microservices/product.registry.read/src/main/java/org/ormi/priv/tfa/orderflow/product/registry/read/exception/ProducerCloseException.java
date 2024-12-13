package org.ormi.priv.tfa.orderflow.product.registry.read.exception;

/**
 * A generic exception class for handling errors when closing a Pulsar producer.
 */
public class ProducerCloseException extends RuntimeException {

    /**
     * Constructs a new {@code ProducerCloseException} with the specified detail message.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public ProducerCloseException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ProducerCloseException} with the specified detail message and cause.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     * @param cause the cause of the exception, which is saved for later retrieval by the {@link Throwable#getCause()} method.
     */
    public ProducerCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
