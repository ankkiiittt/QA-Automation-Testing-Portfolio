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
 * REST Assured API tests for /api/brandsList endpoint.
 */
public class BrandsApiTest extends BaseApiTest {

    @Test(description = "API - GET All Brands List returns 200 OK and validates brands structure", groups = {"API", "Smoke"})
    public void testGetAllBrandsListSuccess() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/brandsList")
                .then()
                .statusCode(200)
                .body("responseCode", equalTo(200))
                .body("brands", not(empty()))
                .extract().response();

        // JSON Schema Assertion
        InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schemas/brands_schema.json");
        if (schemaStream != null) {
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaStream));
        }

        // Deep assertions on brands list
        List<Map<String, Object>> brands = response.jsonPath().getList("brands");
        Assert.assertNotNull(brands, "Brands list should not be null");
        Assert.assertTrue(brands.size() > 0, "Brands list should contain items");

        Map<String, Object> firstBrand = brands.get(0);
        Assert.assertNotNull(firstBrand.get("id"), "Brand ID must not be null");
        Assert.assertNotNull(firstBrand.get("brand"), "Brand name must not be null");

        log.info("Successfully validated {} brands from /api/brandsList. First brand: {}",
                brands.size(), firstBrand.get("brand"));
    }

    @Test(description = "API - PUT to /api/brandsList returns 405 Method Not Supported (Negative)", groups = {"API", "Negative"})
    public void testPutToAllBrandsListNotSupported() {
        given()
                .spec(requestSpec)
                .when()
                .put("/brandsList")
                .then()
                .statusCode(200)
                .body("responseCode", equalTo(405))
                .body("message", equalTo("This request method is not supported."));

        log.info("Validated negative response for unsupported PUT on /api/brandsList");
    }
}
