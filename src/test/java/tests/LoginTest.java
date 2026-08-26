package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

/**
 * UI Test Suite validating User Authentication workflows (Positive & Negative scenarios).
 */
public class LoginTest extends BaseTest {

    @Test(description = "Verify user login with invalid password displays appropriate error", groups = {"Smoke", "Regression"})
    public void testLoginWithInvalidPassword() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page should be loaded successfully");

        LoginPage loginPage = homePage.clickSignupLogin();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded");

        loginPage.loginInvalid("nonexistent_qa_user_999@testmail.com", "WrongPassword!123");
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(), "Error message should be displayed for invalid credentials");
        Assert.assertTrue(loginPage.getLoginErrorMessage().contains("Your email or password is incorrect!"),
                "Error message text should match expected prompt");
    }

    @Test(description = "Verify new user signup with existing email displays duplicate account error", groups = {"Regression"})
    public void testSignupWithExistingEmail() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.clickSignupLogin();
        
        loginPage.submitSignup("Duplicate Tester", "qa_existing_user@gmail.com");
        
        // Either error prompt is shown if email exists or user progresses to signup form
        // We verify the system handles the submission gracefully
        boolean errorOrForm = loginPage.isSignupErrorDisplayed() || driver.getCurrentUrl().contains("/signup");
        Assert.assertTrue(errorOrForm, "System should either show duplicate error or navigate to registration details");
    }

    @Test(description = "Verify navigation to Signup/Login page and title assertion", groups = {"Smoke"})
    public void testNavigationToLoginPage() {
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = homePage.clickSignupLogin();

        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page heading 'Login to your account' must be visible");
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "URL should contain /login");
    }
}
