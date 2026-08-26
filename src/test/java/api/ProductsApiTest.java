package api;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * REST Assured API tests for /api/productsList endpoint.
 */
public class ProductsApiTest extends BaseApiTest {

    @Test(description = "API - GET All Products List returns 200 OK and valid JSON structure", groups = {"API", "Smoke"})
    public void testGetAllProductsListSuccess() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/productsList")
                .then()
                .statusCode(200)
                .body("responseCode", equalTo(200))
                .body("products", not(empty()))
                .extract().response();

        // JSON Schema Assertion
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schemas/products_schema.json");
        if (schemaStream != null) {
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaStream));
        }

        // Deep body assertions
        List<Map<String, Object>> products = response.jsonPath().getList("products");
        Assert.assertNotNull(products, "Products list should not be null");
        Assert.assertTrue(products.size() > 0, "Products list should contain items");

        Map<String, Object> firstProduct = products.get(0);
        Assert.assertNotNull(firstProduct.get("id"), "Product ID must not be null");
        Assert.assertNotNull(firstProduct.get("name"), "Product Name must not be null");
        Assert.assertNotNull(firstProduct.get("price"), "Product Price must not be null");
        Assert.assertNotNull(firstProduct.get("brand"), "Product Brand must not be null");

        log.info("Successfully validated {} products from /api/productsList. First product: {}",
                products.size(), firstProduct.get("name"));
    }

    @Test(description = "API - POST to /api/productsList returns 405 Method Not Supported (Negative)", groups = {"API", "Negative"})
    public void testPostToAllProductsListNotSupported() {
        given()
                .spec(requestSpec)
                .when()
                .post("/productsList")
                .then()
                .statusCode(200) // Note: Target server responds 200 HTTP status with payload responseCode: 405
                .body("responseCode", equalTo(405))
                .body("message", equalTo("This request method is not supported."));

        log.info("Validated negative response for unsupported POST on /api/productsList");
    }
}
