package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Signup / Login Page.
 */
public class LoginPage extends BasePage {

    // Login Form Locators
    private final By loginHeader = By.xpath("//h2[contains(text(),'Login to your account')]");
    private final By loginEmailInput = By.xpath("//input[@data-qa='login-email']");
    private final By loginPasswordInput = By.xpath("//input[@data-qa='login-password']");
    private final By loginButton = By.xpath("//button[@data-qa='login-button']");
    private final By loginErrorMessage = By.xpath("//p[contains(text(),'Your email or password is incorrect!') or contains(text(),'incorrect')]");

    // Signup Form Locators
    private final By signupHeader = By.xpath("//h2[contains(text(),'New User Signup!')]");
    private final By signupNameInput = By.xpath("//input[@data-qa='signup-name']");
    private final By signupEmailInput = By.xpath("//input[@data-qa='signup-email']");
    private final By signupButton = By.xpath("//button[@data-qa='signup-button']");
    private final By signupErrorMessage = By.xpath("//p[contains(text(),'Email Address already exist!')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginPageLoaded() {
        dismissAdIfPresent();
        return isDisplayed(loginHeader);
    }

    /**
     * Performs user login
     *
     * @param email    user email
     * @param password user password
     * @return HomePage instance
     */
    public HomePage login(String email, String password) {
        log.info("Attempting login with email: {}", email);
        dismissAdIfPresent();
        type(loginEmailInput, email);
        type(loginPasswordInput, password);
        click(loginButton);
        dismissAdIfPresent();
        return new HomePage(driver);
    }

    /**
     * Performs invalid login attempt and returns this page
     */
    public LoginPage loginInvalid(String email, String password) {
        log.info("Attempting invalid login with email: {}", email);
        dismissAdIfPresent();
        type(loginEmailInput, email);
        type(loginPasswordInput, password);
        click(loginButton);
        return this;
    }

    public boolean isLoginErrorDisplayed() {
        return isDisplayed(loginErrorMessage);
    }

    public String getLoginErrorMessage() {
        if (isDisplayed(loginErrorMessage)) {
            return getText(loginErrorMessage);
        }
        return "";
    }

    /**
     * Submits new user signup form
     */
    public void submitSignup(String name, String email) {
        log.info("Attempting signup with name: {} and email: {}", name, email);
        dismissAdIfPresent();
        type(signupNameInput, name);
        type(signupEmailInput, email);
        click(signupButton);
        dismissAdIfPresent();
    }

    public boolean isSignupErrorDisplayed() {
        return isDisplayed(signupErrorMessage);
    }

    public String getSignupErrorMessage() {
        if (isDisplayed(signupErrorMessage)) {
            return getText(signupErrorMessage);
        }
        return "";
    }
}
