package org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.resource;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer as PulsarConsumer;
import java.util.function.Consumer as FunctionalConsumer;

import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.ProductRegisteredEventDto;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.ProductRemovedEventDto;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.ProductUpdatedEventDto;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.RemoveProductCommandDto;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.UpdateProductCommandDto;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.mapper.ProductRegistryCommandDtoMapper;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.dto.mapper.ProductRegistryEventDtoMapper;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.resource.exception.ProductRegistryEventStreamException;
import org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.resource.exception.ConsumerCreationException;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.ProductRegistryCommand;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.RegisterProduct;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.RemoveProduct;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.UpdateProduct;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistered;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistryEvent;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRemoved;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductUpdated;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.config.ProductRegistryEventChannelName;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.query.config.ProductRegistryQueryChannelName;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.pulsar.PulsarClientService;
import io.smallrye.reactive.messaging.pulsar.PulsarOutgoingMessage;
import io.smallrye.reactive.messaging.pulsar.PulsarOutgoingMessageMetadata;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * A processor that receives, processes, and emits events from a Pulsar consumer.
 * 
 * @param <T> The event type.
 * 
 * <p>This class allows configuration of event source, emitter, timeout, and handlers
 * for termination, errors, and events.</p>
 */
public class ProductRegistryProcessor<T> {

  /** The Pulsar consumer used to receive events. */
  private PulsarConsumer<T> consumer;

  /** The emitter where processed events are sent. */
  private Emitter emitter;

  /** Timeout in milliseconds for receiving events. */
  private int timeout;

  /** Callback executed on termination. */
  private Runnable onTermination;

  /** Callback executed on error. */
  private FunctionalConsumer<Throwable> onError;

  /** Callback executed when an event is received. */
  private FunctionalConsumer<T> onEvent;

  /**
   * Sets the Pulsar consumer.
   * 
   * @param consumer The Pulsar consumer.
   * @return The current ProductRegistryProcessor instance.
   */
  public ProductRegistryProcessor<T> from(PulsarConsumer<T> consumer) {
      this.consumer = consumer;
      return this;
  }

  /**
   * Sets the emitter.
   * 
   * @param emitter The emitter.
   * @return The current ProductRegistryProcessor instance.
   */
  public ProductRegistryProcessor<T> withEmitter(Emitter emitter) {
      this.emitter = emitter;
      return this;
  }

  /**
   * Sets the timeout for receiving events.
   * 
   * @param timeout The timeout in milliseconds.
   * @return The current ProductRegistryProcessor instance.
   */
  public ProductRegistryProcessor<T> timeout(int timeout) {
      this.timeout = timeout;
      return this;
  }

  /**
   * Sets the termination callback.
   * 
   * @param onTermination The termination callback.
   * @return The current ProductRegistryProcessor instance.
   */
  public ProductRegistryProcessor<T> onTermination(Runnable onTermination) {
      this.onTermination = onTermination;
      return this;
  }

  /**
   * Sets the error callback.
   * 
   * @param onError The error callback.
   * @return The current ProductRegistryProcessor instance.
   */
  public ProductRegistryProcessor<T> onError(FunctionalConsumer<Throwable> onError) {
      this.onError = onError;
      return this;
  }

  /**
   * Sets the event callback.
   * 
   * @param onEvent The event callback.
   * @return The current ProductRegistryProcessor instance.
   */
  public ProductRegistryProcessor<T> onEvent(FunctionalConsumer<T> onEvent) {
      this.onEvent = onEvent;
      return this;
  }

  /**
   * Processes events from the Pulsar consumer and emits results.
   * If an event is not received within the timeout, completes the emitter.
   * If an error occurs, calls the error handler and fails the emitter.
   * 
   * @throws PulsarClientException if an error occurs during event processing.
   */
  public void process() throws PulsarClientException {
      while (!emitter.isCancelled()) {
          try {
              final var msg = Optional.ofNullable(consumer.receive(timeout, TimeUnit.MILLISECONDS));
              if (msg.isEmpty()) {
                  // Complete the emitter if no event is received within the timeout
                  emitter.complete();
                  return;
              }

              T event = msg.get().getValue();
              if (onEvent != null) {
                  onEvent.accept(event);
              }

              // Acknowledge the message
              consumer.acknowledge(msg.get());
          } catch (PulsarClientException e) {
              if (onError != null) {
                  onError.accept(e);
              }
              emitter.fail(e);
              return;
          }
      }
  }
}

@Path("/product/registry")
public class ProductRegistryCommandResource {

  /**
   * Pulsar client service for creating consumers and producers.
   */
  @Inject
  PulsarClientService pulsarClients;

  /**
   * Static emitter for sending product registry commands.
   */
  @Inject
  @Channel("product-registry-command")
  Emitter<ProductRegistryCommand> commandEmitter;

  /**
   * The timeout value for processing events, retrieved from the configuration.
   * Default value is 10000 milliseconds if not specified.
   */
  @ConfigProperty(name = "product.registry.command.timeout", defaultValue = "10000")
  private int timeout;

  /**
   * Endpoint to register a product.
   * 
   * @param cmdDto - DTO containing the product details
   * @return Response indicating the product registration was accepted
   */
  @POST
  @Path("/registerProduct")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerProduct(RegisterProductCommandDto cmdDto, @Context UriInfo uriInfo) {
    final RegisterProduct registerProduct = ProductRegistryCommandDtoMapper.INSTANCE.toCommand(cmdDto);
    final String correlationId = UUID.randomUUID().toString();
    commandEmitter.send(
        PulsarOutgoingMessage.from(Message.of(registerProduct))
            .addMetadata(PulsarOutgoingMessageMetadata.builder()
                .withProperties(Map.of("correlation-id", correlationId))
                .build()));
    return Response
        .seeOther(
            uriInfo.getBaseUriBuilder()
                .path(this.getClass())
                .path("/events/productRegistered")
                .queryParam("correlationId", correlationId)
                .build())
        .build();
  }

  /**
   * Endpoint to stream product registry registered events.
   * 
   * @param correlationId - correlation id to use for the consumer
   * @return Multi of product registry events
   */
  @GET
  @Path("/events/productRegistered")
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  public Multi<ProductRegisteredEventDto> registeredEventStream(@QueryParam("correlationId") String correlationId) {
    // Create a stream of product registry events
    return Multi.createFrom().emitter(em -> {
      // Create consumer for product registry events with the given correlation id
      final PulsarConsumer<Message> consumer = getEventsConsumerByCorrelationId(correlationId);
      // Close the consumer on termination
      em.onTermination(() -> {
        try {
          consumer.unsubscribe();
        } catch (PulsarClientException e) {
          Log.error("Failed to close consumer for product registry events.", e);
        }
      });
      // Consume events and emit DTOs
      CompletableFuture.runAsync(() -> {
        try {
          new ProductRegistryProcessor<ProductRegistryEvent>()
              .from(consumer)
              .withEmitter(em)
              .timeout(timeout)
              .onTermination(() -> Log.debug("Processing terminated"))
              .onError(e -> {
                  Log.error("Error processing event", e);
                  em.fail(e);
              })
              .onEvent(event -> {
                  if (event instanceof ProductRegistryError) {
                    em.fail((ProductRegistryError) event);
                  }
                  else if (event instanceof ProductRegistryEvent){ // if message is event
                    if (event.property.name instanceof ProductRegistered registered){ // if event is ProductRegistered
                      Log.debug("Emitting DTO for registered event: " + registered);
                      // Emit DTO for registered event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(registered));
                    }
                    else if (event.property.name instanceof ProductUpdated updated){ // if event is ProductUpdated
                      Log.debug("Emitting DTO for updated event: " + updated);
                      // Emit DTO for updated event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(updated));
                    }
                    else { // if event is ProductRemoved
                      Log.debug("Emitting DTO for removed event: " + event.property.name);
                      // Emit DTO for removed event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(event.property.name));
                    }
                  }
              })
              .process();
        }
        catch (PulsarClientException e) {
            Log.error("Failed to process product registry events.", e);
            em.fail(e);
        }
      });
    });
  }

  /**
   * Endpoint to update a product.
   * 
   * @param updateProduct - DTO containing the product details
   * @param uriInfo       - URI info for building the response URI
   * @return Response indicating the product update was accepted
   */
  @POST
  @Path("/updateProduct")
  @Consumes("application/json")
  public Response updateProduct(UpdateProductCommandDto updateProduct, @Context UriInfo uriInfo) {
    final UpdateProduct updateProductCommand = ProductRegistryCommandDtoMapper.INSTANCE.toCommand(updateProduct);
    final String correlationId = UUID.randomUUID().toString();
    commandEmitter.send(
        PulsarOutgoingMessage.from(Message.of(updateProductCommand))
            .addMetadata(PulsarOutgoingMessageMetadata.builder()
                .withProperties(Map.of("correlation-id", correlationId))
                .build()));
    return Response
        .seeOther(
            uriInfo.getBaseUriBuilder()
                .path(this.getClass())
                .path("/events/updated?correlationId=" + correlationId)
                .build())
        .build();
  }

  /**
   * Endpoint to stream product registry updated events.
   * 
   * @param correlationId - correlation id to use for the consumer
   * @return Multi of product registry events
   */
  @GET
  @Path("/events/productUpdated")
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  public Multi<ProductUpdatedEventDto> updatedEventStream(@QueryParam("correlationId") String correlationId) {
    // Create a stream of product registry events
    return Multi.createFrom().emitter(em -> {
      // Create consumer for product registry events with the given correlation id
      final PulsarConsumer<ProductRegistryEvent> consumer = getEventsConsumerByCorrelationId(correlationId);
      // Close the consumer on termination
      em.onTermination(() -> {
        try {
          consumer.unsubscribe();
        } catch (PulsarClientException e) {
          Log.error("Failed to close consumer for product registry events.", e);
        }
      });
      // Consume events and emit DTOs
      CompletableFuture.runAsync(() -> {
        try {
          new ProductRegistryProcessor<ProductRegistryEvent>()
              .from(consumer)
              .withEmitter(em)
              .timeout(timeout)
              .onTermination(() -> Log.debug("Processing terminated"))
              .onError(e -> {
                  Log.error("Error processing event", e);
                  em.fail(e);
              })
              .onEvent(event -> {
                  if (event instanceof ProductRegistryError) {
                    em.fail((ProductRegistryError) event);
                  }
                  else if (event instanceof ProductRegistryEvent){ // if message is event
                    if (event.property.name instanceof ProductRegistered registered){ // if event is ProductRegistered
                      Log.debug("Emitting DTO for registered event: " + registered);
                      // Emit DTO for registered event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(registered));
                    }
                    else if (event.property.name instanceof ProductUpdated updated){ // if event is ProductUpdated
                      Log.debug("Emitting DTO for updated event: " + updated);
                      // Emit DTO for updated event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(updated));
                    }
                    else { // if event is ProductRemoved
                      Log.debug("Emitting DTO for removed event: " + event.property.name);
                      // Emit DTO for removed event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(event.property.name));
                    }
                  }
              })
              .process();
        }
        catch (PulsarClientException e) {
            Log.error("Failed to process product registry events.", e);
            em.fail(e);
        }
      });
    });
  }

  /**
   * Endpoint to remove a product.
   * 
   * @param removeProduct - DTO containing the product details
   * @param uriInfo       - URI info for building the response URI
   * @return Response indicating the product removal was accepted
   */
  @POST
  @Path("/removeProduct")
  @Consumes("application/json")
  public Response removeProduct(RemoveProductCommandDto removeProduct, @Context UriInfo uriInfo) {
    final RemoveProduct removeProductCommand = ProductRegistryCommandDtoMapper.INSTANCE.toCommand(removeProduct);
    final String correlationId = UUID.randomUUID().toString();
    commandEmitter.send(
        PulsarOutgoingMessage.from(Message.of(removeProductCommand))
            .addMetadata(PulsarOutgoingMessageMetadata.builder()
                .withProperties(Map.of("correlation-id", correlationId))
                .build()));
    return Response
        .seeOther(
            uriInfo.getBaseUriBuilder()
                .path(this.getClass())
                .path("/events/removed?correlationId=" + correlationId)
                .build())
        .build();
  }

  @GET
  @Path("/events/productRemoved")
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  public Multi<ProductRemovedEventDto> removedEventStream(@QueryParam("correlationId") String correlationId) {
    // Create a stream of product registry events
    return Multi.createFrom().emitter(em -> {
      // Create consumer for product registry events with the given correlation id
      final PulsarConsumer<ProductRegistryEvent> consumer = getEventsConsumerByCorrelationId(correlationId);
      // Close the consumer on termination
      em.onTermination(() -> {
        try {
          consumer.unsubscribe();
        } catch (PulsarClientException e) {
          Log.error("Failed to close consumer for product registry events.", e);
        }
      });
      // Consume events and emit DTOs
      CompletableFuture.runAsync(() -> {
        try {
          new ProductRegistryProcessor<ProductRegistryEvent>()
              .from(consumer)
              .withEmitter(em)
              .timeout(timeout)
              .onTermination(() -> Log.debug("Processing terminated"))
              .onError(e -> {
                  Log.error("Error processing event", e);
                  em.fail(e);
              })
              .onEvent(event -> {
                  if (event instanceof ProductRegistryError) {
                    em.fail((ProductRegistryError) event);
                  }
                  else if (event instanceof ProductRegistryEvent){ // if message is event
                    if (event.property.name instanceof ProductRegistered registered){ // if event is ProductRegistered
                      Log.debug("Emitting DTO for registered event: " + registered);
                      // Emit DTO for registered event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(registered));
                    }
                    else if (event.property.name instanceof ProductUpdated updated){ // if event is ProductUpdated
                      Log.debug("Emitting DTO for updated event: " + updated);
                      // Emit DTO for updated event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(updated));
                    }
                    else { // if event is ProductRemoved
                      Log.debug("Emitting DTO for removed event: " + event.property.name);
                      // Emit DTO for removed event
                      em.emit(ProductRegistryEventDtoMapper.INSTANCE.toDto(event.property.name));
                    }
                  }
              })
              .process();
        }
        catch (PulsarClientException e) {
            Log.error("Failed to process product registry events.", e);
            em.fail(e);
        }
      });
    });
  }

  /**
   * Create a consumer for product registry events with the given correlation id.
   * 
   * Useful for consuming events with a specific correlation id to avoid consuming
   * events from other
   * producers.
   * 
   * @param correlationId - correlation id to use for the consumer
   * @return Consumer for product registry events
   */
  private PulsarConsumer<Message> getEventsConsumerByCorrelationId(String correlationId) {
    try {
      // Define the channel name, topic and schema for the consumer
      final String channelName = ProductRegistryQueryChannelName.PRODUCT_REGISTRY_READ_RESULT.toString();
      final String topic = channelName + "-" + correlationId;
      // Create and return the subscription (consumer)
      return pulsarClients.getClient(channelName)
          .newConsumer(Schema.JSON(ProductRegistryEvent.class))
          .subscriptionName(topic)
          .topic(topic)
          .subscribe();
    } catch (PulsarClientException e) {
      throw new ConsumerCreationException("Failed to create consumer for product registry events.", e);
    }
  }
}
