package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import org.ormi.priv.tfa.orderflow.lib.event.sourcing.store.EventStore;
import org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model.ProductRegistryEventEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;

/**
 * Repository for handling "ProductRegistryEventEntity" events.
 * This repository is responsible for saving events and querying events
 * based on the aggregate root ID and version.
 */
@ApplicationScoped
public class ProductRegistryEventRepository
    implements EventStore<ProductRegistryEventEntity>,
    PanacheMongoRepository<ProductRegistryEventEntity> {

  /**
   * Saves an event in the repository.
   * @param event The event to be saved.
   */
  @Override
  public void saveEvent(ProductRegistryEventEntity event) {
    persist(event);
  }

  /**
   * Finds events by the aggregate root ID and starting version.
   * This query retrieves a list of events for a specific aggregate root
   * ID where the event version is greater than the provided starting version.
   * 
   * @param aggregateRootId The ID of the aggregate root for the events.
   * @param startingVersion The version to start the search from.
   * @return A list of "ProductRegistryEventEntity" events that match the query.
   */
  @Override
  public List<ProductRegistryEventEntity> findEventsByAggregateRootIdAndStartingVersion(String aggregateRootId,
      long startingVersion) {
    return find(
        "aggregateRootId = ?1 and version > ?2",
        aggregateRootId,
        startingVersion,
        Sort.by("version"))
        .list();
  }

}
