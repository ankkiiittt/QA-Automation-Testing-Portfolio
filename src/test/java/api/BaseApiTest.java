package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import listeners.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import utils.ConfigReader;

/**
 * Base API Test class providing REST Assured configuration and specifications.
 */
@Listeners(TestListener.class)
public abstract class BaseApiTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected static RequestSpecification requestSpec;

    @BeforeClass(alwaysRun = true)
    public void setupApi() {
        String baseUri = ConfigReader.get("apiBaseUrl", "https://automationexercise.com/api");
        RestAssured.baseURI = baseUri;
        RestAssured.useRelaxedHTTPSValidation();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setAccept(ContentType.JSON)
                .log(LogDetail.METHOD)
                .log(LogDetail.URI)
                .build();

        log.info("Configured REST Assured with Base URI: {}", baseUri);
    }
}
