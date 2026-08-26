package api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * REST Assured API tests for /api/searchProduct endpoint.
 */
public class SearchProductApiTest extends BaseApiTest {

    @Test(description = "API - POST to /api/searchProduct with valid parameter returns matching products", groups = {"API", "Smoke"})
    public void testSearchProductWithValidQuery() {
        String searchTerm = "top";
        Response response = given()
                .spec(requestSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParam("search_product", searchTerm)
                .when()
                .post("/searchProduct")
                .then()
                .statusCode(200)
                .body("responseCode", equalTo(200))
                .body("products", not(empty()))
                .extract().response();

        List<Map<String, Object>> products = response.jsonPath().getList("products");
        Assert.assertNotNull(products, "Searched products list should not be null");
        Assert.assertTrue(products.size() > 0, "Searched products list should contain at least 1 item");

        for (Map<String, Object> product : products) {
            String name = (String) product.get("name");
            Assert.assertNotNull(name, "Product name should not be null");
            Assert.assertTrue(name.toLowerCase().contains(searchTerm.toLowerCase()),
                    "Product name '" + name + "' should contain search term '" + searchTerm + "'");
        }

        log.info("Successfully searched products for term '{}', found {} matches", searchTerm, products.size());
    }

    @Test(description = "API - POST to /api/searchProduct without parameter returns 400 Bad Request (Negative)", groups = {"API", "Negative"})
    public void testSearchProductWithoutParameterReturns400() {
        given()
                .spec(requestSpec)
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post("/searchProduct")
                .then()
                .statusCode(200) // Target server returns 200 HTTP status with payload responseCode: 400
                .body("responseCode", equalTo(400))
                .body("message", equalTo("Bad request, search_product parameter is missing in POST request."));

        log.info("Validated negative response for search without parameter on /api/searchProduct");
    }
}
