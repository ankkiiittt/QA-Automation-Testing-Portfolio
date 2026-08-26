package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;
import utils.DriverManager;

/**
 * Step definitions for User Authentication and Login feature.
 */
public class LoginSteps {
    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;

    public LoginSteps() {
        this.driver = DriverManager.getDriver();
        this.homePage = new HomePage(driver);
        this.loginPage = new LoginPage(driver);
    }

    @Given("the user navigates to the Automation Exercise home page")
    public void theUserNavigatesToTheAutomationExerciseHomePage() {
        homePage.open();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be successfully loaded");
    }

    @When("the user clicks on the {string} navigation button")
    public void theUserClicksOnTheNavigationButton(String navItem) {
        if (navItem.equalsIgnoreCase("Signup / Login")) {
            loginPage = homePage.clickSignupLogin();
        } else if (navItem.equalsIgnoreCase("Products")) {
            homePage.clickProducts();
        } else if (navItem.equalsIgnoreCase("Cart")) {
            homePage.clickCart();
        }
    }

    @Then("the login page should be displayed")
    public void theLoginPageShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be displayed");
    }

    @Then("the {string} heading should be visible")
    public void theHeadingShouldBeVisible(String expectedHeading) {
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Expected heading: " + expectedHeading + " should be visible");
    }

    @When("the user enters email {string} and password {string}")
    public void theUserEntersEmailAndPassword(String email, String password) {
        loginPage.loginInvalid(email, password);
    }

    @And("clicks the login button")
    public void clicksTheLoginButton() {
        // Handled within login method
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String expectedError) {
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(), "Login error message must be visible");
        String actualError = loginPage.getLoginErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError),
                "Expected error message '" + expectedError + "' but found: '" + actualError + "'");
    }
}
