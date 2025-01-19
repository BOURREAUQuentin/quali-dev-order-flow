package org.ormi.priv.tfa.orderflow.product.registry.errors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.ProductRegistryCommand;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.RegisterProduct;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.RemoveProduct;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.UpdateProduct;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistered;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRegistryEvent;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductRemoved;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.ProductUpdated;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.valueobject.ProductId;
import org.ormi.priv.tfa.orderflow.product.registry.aggregate.service.ProductRegistryService;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

/**
 * Represents a generic processing error in the ProductRegistry.
 */
public record ProductRegistryError implements ChannelMessage (String code, String message) {
    @Override
    public String toString() {
        return "ProductRegistryError{" +
                "errorcode='" + code + '\'' +
                ", errormessage='" + message + '\'' +
                '}';
    }
}
