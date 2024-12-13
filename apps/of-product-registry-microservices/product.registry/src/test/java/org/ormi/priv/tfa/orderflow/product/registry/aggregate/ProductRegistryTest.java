package org.ormi.priv.tfa.orderflow.product.registry.aggregate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.command.*;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.event.*;
import org.ormi.priv.tfa.orderflow.lib.publishedlanguage.valueobject.ProductId;
import org.ormi.priv.tfa.orderflow.product.registry.aggregate.service.ProductRegistryService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ProductRegistry class.
 * These tests validate the business logic of the ProductRegistry aggregate.
 */
class ProductRegistryTest {

    // Constants for repeated literals
    private static final String PRODUCT_ID = "12345";
    private static final String PRODUCT_NAME = "Product Name";
    private static final String PRODUCT_DESCRIPTION = "Product Description";
    private static final String OTHER_PRODUCT_NAME = "Other Product Name";

    private ProductRegistry productRegistry;
    private ProductRegistryService productRegistryService;

    /**
     * Set up the test environment by initializing the ProductRegistry and its dependencies.
     * Mock the ProductRegistryService to isolate tests.
     */
    @BeforeEach
    void setUp() {
        productRegistryService = mock(ProductRegistryService.class);
        productRegistry = new ProductRegistry(productRegistryService);
    }

    /**
     * Test handling a valid RegisterProduct command.
     * Verifies that the product is correctly added to the registry.
     */
    @Test
    void testHandleValidRegisterProductCommand() {
        // Prepare a valid RegisterProduct command and its corresponding event
        ProductId productId = new ProductId(PRODUCT_ID);
        RegisterProduct command = new RegisterProduct(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION);
        ProductRegistered event = new ProductRegistered(command);

        // Mock the behavior of the ProductRegistryService to return the expected event
        when(productRegistryService.registerProduct(eq(productRegistry), eq(command)))
            .thenReturn(Uni.createFrom().item(event));

        // Handle the command and observe the result asynchronously
        productRegistry.handle(command)
            .onItem().invoke(result -> {
                // Verify that the product is successfully added to the registry
                assertTrue(productRegistry.hasProductWithId(productId));
            }).subscribe().with(result -> { });
    }

    /**
     * Test handling an invalid command type.
     * Verifies that an exception is thrown for unrecognized commands.
     */
    @Test
    void testHandleInvalidCommand() {
        // Create a mock command of an unrecognized type
        ProductRegistryCommand invalidCommand = mock(ProductRegistryCommand.class);

        // Ensure that handling an invalid command throws an exception
        assertThrows(IllegalArgumentException.class, () -> 
            productRegistry.handle(invalidCommand).await().indefinitely());
    }

    /**
     * Test applying a valid ProductRegistered event.
     * Verifies that the product is correctly added to the registry.
     */
    @Test
    void testApplyValidProductRegisteredEvent() {
        // Create a ProductRegistered event
        ProductId productId = new ProductId(PRODUCT_ID);
        ProductRegistered event = new ProductRegistered(new RegisterProduct(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION));

        // Apply the event to the ProductRegistry
        productRegistry.apply(event);

        // Verify that the product is now present in the registry
        assertTrue(productRegistry.hasProductWithId(productId));
    }

    /**
     * Test applying a ProductRemoved event.
     * Verifies that the product is correctly removed from the registry.
     */
    @Test
    void testApplyProductRemovedEvent() {
        // Add a product to the registry by applying a ProductRegistered event
        ProductId productId = new ProductId(PRODUCT_ID);
        productRegistry.apply(new ProductRegistered(new RegisterProduct(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION)));

        // Create a ProductRemoved event for the same product
        ProductRemoved event = new ProductRemoved(new RemoveProduct(productId));

        // Apply the ProductRemoved event
        productRegistry.apply(event);

        // Verify that the product is no longer present in the registry
        assertFalse(productRegistry.hasProductWithId(productId));
    }

    /**
     * Test retrieving the current version of the registry.
     * Verifies that the version starts at 0 and increments correctly.
     */
    @Test
    void testGetVersion() {
        // Retrieve the current version of the registry
        long version = productRegistry.getVersion();

        // Verify that the initial version is 0
        assertEquals(0, version);

        // Increment the version and verify the change
        productRegistry.incrementVersion();
        assertEquals(1, productRegistry.getVersion());
    }

    /**
     * Test checking for a product by ID.
     * Verifies that the product is correctly found in the registry.
     */
    @Test
    void testHasProductWithId() {
        // Add a product to the registry by applying a ProductRegistered event
        ProductId productId = new ProductId(PRODUCT_ID);
        productRegistry.apply(new ProductRegistered(new RegisterProduct(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION)));

        // Check that the product is found by its ID
        assertTrue(productRegistry.hasProductWithId(productId));
    }

    /**
     * Test checking for a product instance in the registry.
     * Verifies that the product is correctly found.
     */
    @Test
    void testHasProduct() {
        // Add a product to the registry by applying a ProductRegistered event
        ProductId productId = new ProductId(PRODUCT_ID);
        Product product = new Product(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION);
        productRegistry.apply(new ProductRegistered(new RegisterProduct(productId, PRODUCT_NAME, PRODUCT_DESCRIPTION)));

        // Check that the product is found in the registry
        assertTrue(productRegistry.hasProduct(product));
    }

    /**
     * Test checking for product name availability.
     * Verifies that the name is correctly identified as available or unavailable.
     */
    @Test
    void testIsProductNameAvailable() {
        // Add a product with a specific name to the registry
        productRegistry.apply(new ProductRegistered(new RegisterProduct(new ProductId(PRODUCT_ID), PRODUCT_NAME, PRODUCT_DESCRIPTION)));

        // Verify the availability of product names
        assertFalse(productRegistry.isProductNameAvailable(PRODUCT_NAME)); // Name is already taken
        assertTrue(productRegistry.isProductNameAvailable(OTHER_PRODUCT_NAME)); // Name is available
    }
}
