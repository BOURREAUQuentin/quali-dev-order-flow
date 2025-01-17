package org.ormi.priv.tfa.orderflow.product.registry.read.service;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistryEvent;
import org.ormi.priv.tfa.orderflow.product.registry.read.projection.ProductRegistryProjector;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductRegistryEventConsumer {

  @Inject
  private ProductRegistryProjector projector;

  @Incoming("product-registry-event")
  @Transactional(Transactional.TxType.REQUIRED)
  public void handleEvent(ProductRegistryEvent event) {
    // Project the event
    projector.handleEvent(event);

    // Get the producer for the correlation id
    getEventSinkByCorrelationId(correlationId)
      .thenAccept((producer) -> {
        // Sink the event
        producer
            .newMessage()
            .value(event)
            .sendAsync()
            .whenComplete((msgId, ex) -> {
              if (ex != null) {
                throw new RuntimeException("Failed to produce event for correlation id: " + correlationId, ex);
              }
              Log.debug(String.format("Sinked event with correlation id{%s} in msg{%s}", correlationId, msgId));
              try {
                producer.close();
              } catch (PulsarClientException e) {
                throw new RuntimeException("Failed to close producer", e);
              }
            });
      });
  }
}
