package org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface scellée pour représenter les messages du canal (événements et erreurs).
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = ProductRegistryEvent.class, name = "ProductRegistryEvent"),
  @JsonSubTypes.Type(value = ProductRegistryError.class, name = "ProductRegistryError")
})
public sealed interface ChannelMessage permits ProductRegistryEvent, ProductRegistryError {
}
