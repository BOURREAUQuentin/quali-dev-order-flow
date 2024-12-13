package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

/**
 * Represents an entity for the "ProductRegistered" event.
 * This event is used to register a product in the product registry.
 */
public class ProductRegisteredEventEntity extends ProductRegistryEventEntity {

    /**
     * The event type, used to identify that this is a "ProductRegistered" event.
     */
    static final String EVENT_TYPE = "ProductRegistered";

    /**
     * Represents the payload of the "ProductRegistered" event.
     * Contains the specific details of the product being registered.
     */
    public record Payload(
        String productId,
        String name,
        String productDescription
    ) {}

    /**
     * The payload for the event.
     */
    private Payload payload;

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    /**
     * Getter for the payload.
     * @return the payload of the event.
     */
    public Payload getPayload() {
        return payload;
    }

    /**
     * Setter for the payload.
     * @param payload the payload to set.
     */
    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
