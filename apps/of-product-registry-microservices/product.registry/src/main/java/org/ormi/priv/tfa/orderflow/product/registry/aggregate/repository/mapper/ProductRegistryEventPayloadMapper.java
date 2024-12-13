package org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistered;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRemoved;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductUpdated;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.valueobject.mapper.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model.ProductRegisteredEventEntity;
import org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model.ProductRemovedEventEntity;
import org.ormi.priv.tfa.orderflow.product.registry.aggregate.repository.model.ProductUpdatedEventEntity;

/**
 * Mapper interface for converting between business event payloads (ProductRegistered.Payload,
 * ProductUpdated.Payload, ProductRemoved.Payload) and their corresponding database entity payloads 
 * (ProductRegisteredEventEntity.Payload, ProductUpdatedEventEntity.Payload, ProductRemovedEventEntity.Payload)
 */
@Mapper(uses = {ProductIdMapper.class})
public interface ProductRegistryEventPayloadMapper {
    /**
     * Maps the payload of a ProductRegistered business event to the corresponding database entity payload
     *
     * @param eventPayload the payload of a ProductRegistered event
     * @return the corresponding ProductRegisteredEventEntity.Payload for database storage
     */
    @Named("productRegisteredEventPayloadToEntity")
    @Mapping(target = "productId", source = "productId", qualifiedByName = "productIdToString")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "productDescription", source = "productDescription")
    public ProductRegisteredEventEntity.Payload toEntity(ProductRegistered.Payload eventPayload);

    /**
     * Maps the payload of a ProductRegisteredEventEntity from the database to a ProductRegistered business event payload
     *
     * @param entityPayload the payload of a ProductRegisteredEventEntity from the database
     * @return the corresponding ProductRegistered.Payload event payload
     */
    @Named("productRegisteredEventPayloadToEvent")
    @Mapping(target = "productId", source = "productId", qualifiedByName = "toProductId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "productDescription", source = "productDescription")
    public ProductRegistered.Payload toEvent(ProductRegisteredEventEntity.Payload entityPayload);

    /**
     * Maps the payload of a ProductUpdated business event to the corresponding database entity payload
     *
     * @param eventPayload the payload of a ProductUpdated event
     * @return the corresponding ProductUpdatedEventEntity.Payload for database storage
     */
    @Named("productUpdatedEventEntityToEntity")
    @Mapping(target = "productId", source = "productId", qualifiedByName = "productIdToString")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "productDescription", source = "productDescription")
    public ProductUpdatedEventEntity.Payload toDto(ProductUpdated.Payload eventPayload);

    /**
     * Maps the payload of a ProductUpdatedEventEntity from the database to a ProductUpdated business event payload
     *
     * @param entityPayload the payload of a ProductUpdatedEventEntity from the database
     * @return the corresponding ProductUpdated.Payload event payload
     */
    @Named("productUpdatedEventPayloadToEvent")
    @Mapping(target = "productId", source = "productId", qualifiedByName = "toProductId")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "productDescription", source = "productDescription")
    public ProductUpdated.Payload toEntity(ProductUpdatedEventEntity.Payload entityPayload);

    /**
     * Maps the payload of a ProductRemoved business event to the corresponding database entity payload
     *
     * @param eventPayload the payload of a ProductRemoved event
     * @return the corresponding ProductRemovedEventEntity.Payload for database storage
     */
    @Named("productRemovedEventPayloadToEntity")
    @Mapping(target = "productId", source = "productId", qualifiedByName = "productIdToString")
    public ProductRemovedEventEntity.Payload toEntity(ProductRemoved.Payload eventPayload);

    /**
     * Maps the payload of a ProductRemovedEventEntity from the database to a ProductRemoved business event payload
     *
     * @param entityPayload the payload of a ProductRemovedEventEntity from the database
     * @return the corresponding ProductRemoved.Payload event payload
     */
    @Named("productRemovedEventPayloadToEvent")
    @Mapping(target = "productId", source = "productId", qualifiedByName = "toProductId")
    public ProductRemoved.Payload toEvent(ProductRemovedEventEntity.Payload entityPayload);
}
