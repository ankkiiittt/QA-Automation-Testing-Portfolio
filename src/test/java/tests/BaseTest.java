package tests;

import listeners.TestListener;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ConfigReader;
import utils.DriverManager;

/**
 * Base Test class configuring WebDriver lifecycle for UI regression tests.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        log.info("Setting up WebDriver instance for test execution");
        driver = DriverManager.initDriver();
        String baseUrl = ConfigReader.get("baseUrl", "https://automationexercise.com");
        driver.get(baseUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("Tearing down WebDriver instance");
        DriverManager.quitDriver();
    }
}
