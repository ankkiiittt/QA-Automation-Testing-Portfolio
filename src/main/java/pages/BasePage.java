package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

/**
 * Base Page providing common Selenium actions, explicit waits,
 * JavaScript interactions, and resilient UI error recovery.
 */
public abstract class BasePage {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        int timeoutSeconds = ConfigReader.getInt("explicitWait", 15);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void click(By locator) {
        try {
            waitForClickable(locator).click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            log.debug("Standard click intercepted on {}, attempting JavaScript click fallback", locator);
            jsClick(locator);
        }
    }

    protected void jsClick(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Dismiss any intrusive Google ad overlay or iframe if present on automationexercise.com
     */
    public void dismissAdIfPresent() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "const adFrames = document.querySelectorAll('iframe[id*=\"aswift\"], iframe[id*=\"google_ads\"]');" +
                    "adFrames.forEach(f => f.remove());" +
                    "const adOverlays = document.querySelectorAll('ins.adsbygoogle, div#dismiss-button');" +
                    "adOverlays.forEach(o => o.remove());"
            );
        } catch (Exception ignored) {
        }
    }
}
