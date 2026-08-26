package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DriverManager;

/**
 * Cucumber Hooks for driver lifecycle management and failure screenshot attachment.
 */
public class Hooks {
    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void setup(Scenario scenario) {
        log.info("Starting Cucumber Scenario: [{}]", scenario.getName());
        DriverManager.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName() + " - Failure Screenshot");
                log.info("Attached failure screenshot for scenario: [{}]", scenario.getName());
            } catch (Exception e) {
                log.warn("Failed to capture screenshot for cucumber failure: {}", e.getMessage());
            }
        }
        log.info("Completed Cucumber Scenario: [{}] with status: {}", scenario.getName(), scenario.getStatus());
        DriverManager.quitDriver();
    }
}
