package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;

/**
 * This is the base class for all product registry event entities, representing
 * a record of events in the product registry system. Each event entity will 
 * be stored in a MongoDB collection named "product_registry_events".
 */
@MongoEntity(collection = "product_registry_events")
public abstract class ProductRegistryEventEntity {

    /**
     * IMPORTANT: Fields in Panache MongoDB entities are public to allow direct access
     * for simplicity and compatibility with the framework. Using private fields
     * would require explicit getters and setters, which goes against Panache's design.
     */

    /**
     * The unique identifier for this event entity in the MongoDB collection.
     * This ID is automatically generated by MongoDB when the entity is stored.
     */
	public ObjectId id;

    /**
     * The event ID, which is typically used to uniquely identify the event 
     * across different systems.
     */
    public String eventId;

    /**
     * The event type, representing the kind of event this entity is related to.
     * This value is populated by calling the abstract method getEventType().
     */
    public String eventType = getEventType();

    /**
     * The aggregate root ID, which is the ID of the product or aggregate entity 
     * that the event is associated with.
     */
    public String aggregateRootId;

    /**
     * The version of the event, typically used for event versioning and conflict 
     * resolution in event sourcing.
     */
    public long version;

    /**
     * The timestamp indicating when the event occurred, used for event ordering 
     * and time-based queries.
     */
    public long timestamp;

    /**
     * This abstract method is meant to be implemented by subclasses to define 
     * the specific event type.
     * 
     * @return the event type as a string (e.g., "ProductRegistered", "ProductUpdated").
     */
    abstract String getEventType();
}
