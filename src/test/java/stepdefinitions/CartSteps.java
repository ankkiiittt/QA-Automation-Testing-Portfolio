package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;
import utils.DriverManager;

/**
 * Step definitions for Product Search and Shopping Cart feature.
 */
public class CartSteps {
    private WebDriver driver;
    private HomePage homePage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    public CartSteps() {
        this.driver = DriverManager.getDriver();
        this.homePage = new HomePage(driver);
        this.productsPage = new ProductsPage(driver);
        this.cartPage = new CartPage(driver);
    }

    @When("the user navigates to the {string} page")
    public void theUserNavigatesToPage(String pageName) {
        if (pageName.equalsIgnoreCase("Products")) {
            productsPage = homePage.clickProducts();
            Assert.assertTrue(productsPage.isProductsPageLoaded(), "Products catalog page should load");
        } else if (pageName.equalsIgnoreCase("Cart")) {
            cartPage = homePage.clickCart();
            Assert.assertTrue(cartPage.isCartPageLoaded(), "Cart page should load");
        }
    }

    @And("searches for product {string}")
    public void searchesForProduct(String keyword) {
        productsPage.searchProduct(keyword);
        Assert.assertTrue(productsPage.getPageHeaderTitle().toUpperCase().contains("SEARCHED PRODUCTS"),
                "Searched products title should be displayed");
    }

    @And("adds the first displayed product to the shopping cart")
    public void addsTheFirstDisplayedProductToTheShoppingCart() {
        productsPage.addProductToCartByIndex(0);
        Assert.assertTrue(productsPage.isAddedToCartModalDisplayed(), "Added to Cart modal should be visible");
    }

    @And("clicks on the {string} modal link")
    public void clicksOnTheModalLink(String linkText) {
        if (linkText.equalsIgnoreCase("View Cart")) {
            cartPage = productsPage.clickViewCartFromModal();
        } else {
            productsPage.clickContinueShopping();
        }
    }

    @Then("the shopping cart page should be displayed")
    public void theShoppingCartPageShouldBeDisplayed() {
        Assert.assertTrue(cartPage.isCartPageLoaded(), "Shopping cart page must be loaded");
    }

    @And("the shopping cart should contain at least {int} item")
    public void theShoppingCartShouldContainAtLeastItem(int expectedMinCount) {
        int count = cartPage.getCartItemCount();
        Assert.assertTrue(count >= expectedMinCount,
                "Cart items count " + count + " should be at least " + expectedMinCount);
    }
}
