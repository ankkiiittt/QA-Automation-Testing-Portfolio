package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;

/**
 * Page Object for Automation Exercise Home Page.
 */
public class HomePage extends BasePage {

    // Locators
    private final By logo = By.xpath("//div[@class='logo pull-left']//img");
    private final By homeNav = By.xpath("//a[contains(text(),'Home')]");
    private final By productsNav = By.xpath("//a[contains(@href,'/products')]");
    private final By cartNav = By.xpath("//a[contains(@href,'/view_cart')]");
    private final By signupLoginNav = By.xpath("//a[contains(@href,'/login')]");
    private final By logoutNav = By.xpath("//a[contains(@href,'/logout')]");
    private final By deleteAccountNav = By.xpath("//a[contains(@href,'/delete_account')]");
    private final By loggedInUserText = By.xpath("//li[contains(.,'Logged in as')]/b");
    private final By subscriptionHeading = By.xpath("//h2[contains(text(),'Subscription')]");
    private final By subscriptionEmailInput = By.id("susbscribe_email");
    private final By subscribeButton = By.id("subscribe");
    private final By subscriptionSuccessAlert = By.xpath("//div[contains(@class,'alert-success') and contains(text(),'You have been successfully subscribed')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        String url = ConfigReader.get("baseUrl", "https://automationexercise.com");
        log.info("Navigating to URL: {}", url);
        driver.get(url);
        dismissAdIfPresent();
        return this;
    }

    public boolean isHomePageLoaded() {
        dismissAdIfPresent();
        return isDisplayed(logo);
    }

    public LoginPage clickSignupLogin() {
        log.info("Clicking Signup / Login button");
        dismissAdIfPresent();
        click(signupLoginNav);
        dismissAdIfPresent();
        return new LoginPage(driver);
    }

    public ProductsPage clickProducts() {
        log.info("Navigating to Products page");
        dismissAdIfPresent();
        click(productsNav);
        dismissAdIfPresent();
        return new ProductsPage(driver);
    }

    public CartPage clickCart() {
        log.info("Navigating to Cart page");
        dismissAdIfPresent();
        click(cartNav);
        dismissAdIfPresent();
        return new CartPage(driver);
    }

    public HomePage clickLogout() {
        log.info("Clicking Logout button");
        dismissAdIfPresent();
        click(logoutNav);
        return this;
    }

    public boolean isLoggedInAs(String expectedUsername) {
        if (!isDisplayed(loggedInUserText)) {
            return false;
        }
        String actual = getText(loggedInUserText);
        return actual.equalsIgnoreCase(expectedUsername);
    }

    public String getLoggedInUsername() {
        if (isDisplayed(loggedInUserText)) {
            return getText(loggedInUserText);
        }
        return "";
    }

    public boolean isLogoutButtonVisible() {
        return isDisplayed(logoutNav);
    }

    public HomePage subscribeToNewsletter(String email) {
        log.info("Subscribing email {} to newsletter", email);
        scrollToElement(subscriptionHeading);
        type(subscriptionEmailInput, email);
        click(subscribeButton);
        return this;
    }

    public boolean isSubscriptionSuccessDisplayed() {
        return isDisplayed(subscriptionSuccessAlert);
    }
}
