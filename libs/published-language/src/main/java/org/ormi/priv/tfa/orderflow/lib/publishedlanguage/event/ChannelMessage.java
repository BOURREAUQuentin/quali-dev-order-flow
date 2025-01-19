package org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Sealed interface for representing channel messages (events and errors).
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
