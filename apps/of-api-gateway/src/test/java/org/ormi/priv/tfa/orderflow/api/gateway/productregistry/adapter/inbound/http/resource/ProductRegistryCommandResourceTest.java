package org.ormi.priv.tfa.orderflow.api.gateway.productregistry.adapter.inbound.http.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for the Product Registry Command Resource API
 * 
 * This class contains tests for the following operations:
 * - Registering a product
 * - Updating a product
 * - Removing a product
 * 
 * The tests ensure that the endpoints respond with appropriate status codes 
 * and headers based on the validity of the provided request payloads
 */
@QuarkusTest
public class ProductRegistryCommandResourceTest {

    /**
     * Valid JSON payload for registering a product.
     */
    private static final String VALID_PRODUCT_REGISTER = """
        {
            "productId": "12345",
            "name": "Test Product",
            "productDescription": "A product for testing."
        }
    """;

    /**
     * Invalid JSON payload for registering a product (missing fields).
     */
    private static final String INVALID_PRODUCT_REGISTER = """
        {
            "productId": "12345"
        }
    """;

    /**
     * Valid JSON payload for updating a product.
     */
    private static final String VALID_PRODUCT_UPDATE = """
        {
            "productId": "12345",
            "name": "Updated Product",
            "productDescription": "Updated description."
        }
    """;

    /**
     * Invalid JSON payload for updating a product (equivalent to INVALID_PRODUCT_REGISTER).
     */
    private static final String INVALID_PRODUCT_UPDATE = INVALID_PRODUCT_REGISTER;

    /**
     * Valid JSON payload for removing a product.
     */
    private static final String VALID_PRODUCT_REMOVE = """
        {
            "productId": "12345"
        }
    """;

    /**
     * Invalid JSON payload for removing a product (missing required field `productId`).
     */
    private static final String INVALID_PRODUCT_REMOVE = """
        {
            "name": "Invalid Product"
        }
    """;

    // Constants for endpoint paths
    private static final String REGISTER_PRODUCT_ENDPOINT = "/product/registry/registerProduct";
    private static final String UPDATE_PRODUCT_ENDPOINT = "/product/registry/updateProduct";
    private static final String REMOVE_PRODUCT_ENDPOINT = "/product/registry/removeProduct";

    private static final String REGISTER_PRODUCT_EVENT = "/product/registry/events/productRegistered";
    private static final String UPDATE_PRODUCT_EVENT = "/product/registry/events/productUpdated";
    private static final String REMOVE_PRODUCT_EVENT = "/product/registry/events/productRemoved";

    /**
     * HTTP header used to verify the redirection location.
     */
    private static final String HEADER_LOCATION = "Location";

    /**
     * Tests registering a product with valid data.
     * Expects a 303 (See Other) response and a valid `Location` header.
     */
    @Test
    void registerProductWithValidData() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(VALID_PRODUCT_REGISTER)
            .post(REGISTER_PRODUCT_ENDPOINT)
            .then()
            .statusCode(303) // HTTP See Other
            .header(HEADER_LOCATION, containsString(REGISTER_PRODUCT_EVENT))
            .log().ifValidationFails();
    }

    /**
     * Tests registering a product with invalid data.
     * Expects a 400 (Bad Request) response.
     */
    @Test
    void registerProductWithInvalidData() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(INVALID_PRODUCT_REGISTER)
            .post(REGISTER_PRODUCT_ENDPOINT)
            .then()
            .statusCode(400) // HTTP Bad Request
            .log().ifValidationFails();
    }

    /**
     * Tests registering a product with an empty request body.
     * Expects a 400 (Bad Request) response.
     */
    @Test
    void registerProductWithNullBody() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("")
            .post(REGISTER_PRODUCT_ENDPOINT)
            .then()
            .statusCode(400) // HTTP Bad Request
            .log().ifValidationFails();
    }

    /**
     * Tests updating a product with valid data.
     * Expects a 303 (See Other) response and a valid `Location` header.
     */
    @Test
    void updateProductWithValidData() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(VALID_PRODUCT_UPDATE)
            .post(UPDATE_PRODUCT_ENDPOINT)
            .then()
            .statusCode(303) // HTTP See Other
            .header(HEADER_LOCATION, containsString(UPDATE_PRODUCT_EVENT))
            .log().ifValidationFails();
    }

    /**
     * Tests updating a product with invalid data.
     * Expects a 400 (Bad Request) response.
     */
    @Test
    void updateProductWithInvalidData() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(INVALID_PRODUCT_UPDATE)
            .post(UPDATE_PRODUCT_ENDPOINT)
            .then()
            .statusCode(400) // HTTP Bad Request
            .log().ifValidationFails();
    }

    /**
     * Tests updating a product with an empty request body.
     * Expects a 400 (Bad Request) response.
     */
    @Test
    void updateProductWithNullBody() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("")
            .post(UPDATE_PRODUCT_ENDPOINT)
            .then()
            .statusCode(400) // HTTP Bad Request
            .log().ifValidationFails();
    }

    /**
     * Tests removing a product with valid data.
     * Expects a 303 (See Other) response and a valid `Location` header.
     */
    @Test
    void removeProductWithValidData() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(VALID_PRODUCT_REMOVE)
            .post(REMOVE_PRODUCT_ENDPOINT)
            .then()
            .statusCode(303) // HTTP See Other
            .header(HEADER_LOCATION, containsString(REMOVE_PRODUCT_EVENT))
            .log().ifValidationFails();
    }

    /**
     * Tests removing a product with invalid data.
     * Expects a 400 (Bad Request) response.
     */
    @Test
    void removeProductWithInvalidData() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(INVALID_PRODUCT_REMOVE)
            .post(REMOVE_PRODUCT_ENDPOINT)
            .then()
            .statusCode(400) // HTTP Bad Request
            .log().ifValidationFails();
    }

    /**
     * Tests removing a product with an empty request body.
     * Expects a 400 (Bad Request) response.
     */
    @Test
    void removeProductWithNullBody() {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body("")
            .post(REMOVE_PRODUCT_ENDPOINT)
            .then()
            .statusCode(400) // HTTP Bad Request
            .log().ifValidationFails();
    }
}