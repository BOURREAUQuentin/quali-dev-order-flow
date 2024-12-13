package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

/**
 * Represents an entity for the "ProductRemoved" event.
 * This event is used to remove a product from the product registry.
 */
public class ProductRemovedEventEntity extends ProductRegistryEventEntity {

  /**
   * The event type, used to identify that this is a "ProductRemoved" event.
   */
  static final String EVENT_TYPE = "ProductRemoved";

  /**
     * Represents the payload of the "ProductRemoved" event.
     * Contains the specific details of the product being removed.
     */
    public record Payload(
      String productId
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
