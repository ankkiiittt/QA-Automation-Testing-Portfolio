package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductsPage;

import java.util.List;

/**
 * UI Test Suite validating Shopping Cart operations and Checkout progression.
 */
public class CartAndCheckoutTest extends BaseTest {

    @Test(description = "Verify adding a product to cart and validating its presence in shopping cart", groups = {"Smoke", "Regression"})
    public void testAddProductToCartAndVerify() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.clickProducts();

        productsPage.addProductToCartByIndex(0);
        Assert.assertTrue(productsPage.isAddedToCartModalDisplayed(), "Added to Cart modal should be displayed");

        CartPage cartPage = productsPage.clickViewCartFromModal();
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should be loaded");

        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(itemCount >= 1, "Cart item count should be at least 1");

        List<String> titles = cartPage.getCartItemTitles();
        log.info("Items in cart: {}", titles);
        Assert.assertFalse(titles.isEmpty(), "Cart item list should not be empty");
    }

    @Test(description = "Verify unauthenticated user proceeding to checkout is prompted to Register/Login", groups = {"Regression"})
    public void testProceedToCheckoutAsGuestPromptsLogin() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.clickProducts();

        productsPage.addProductToCartByIndex(0);
        CartPage cartPage = productsPage.clickViewCartFromModal();

        LoginPage loginPage = cartPage.proceedToCheckoutUnauthenticated();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "User should be navigated to Login/Register page when proceeding unauthenticated");
    }
}
