package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for Shopping Cart.
 */
public class CartPage extends BasePage {

    // Locators
    private final By cartItemsTable = By.id("cart_info_table");
    private final By cartRows = By.xpath("//tbody/tr[starts-with(@id,'product-')]");
    private final By cartItemNames = By.xpath("//tbody/tr[starts-with(@id,'product-')]//td[@class='cart_description']//h4/a");
    private final By cartItemPrices = By.xpath("//tbody/tr[starts-with(@id,'product-')]//td[@class='cart_price']/p");
    private final By cartItemQuantities = By.xpath("//tbody/tr[starts-with(@id,'product-')]//td[@class='cart_quantity']/button");
    private final By proceedToCheckoutButton = By.xpath("//a[contains(text(),'Proceed To Checkout')]");
    private final By checkoutModalRegisterLogin = By.xpath("//div[@id='checkoutModal']//a[contains(@href,'/login')]");
    private final By deleteButtons = By.xpath("//a[@class='cart_quantity_delete']");
    private final By emptyCartText = By.xpath("//span[@id='empty_cart']//b[contains(text(),'Cart is empty')]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCartPageLoaded() {
        dismissAdIfPresent();
        return isDisplayed(cartItemsTable) || isDisplayed(emptyCartText);
    }

    public int getCartItemCount() {
        dismissAdIfPresent();
        return driver.findElements(cartRows).size();
    }

    public List<String> getCartItemTitles() {
        dismissAdIfPresent();
        List<WebElement> elements = driver.findElements(cartItemNames);
        List<String> titles = new ArrayList<>();
        for (WebElement el : elements) {
            titles.add(el.getText().trim());
        }
        return titles;
    }

    public CheckoutPage proceedToCheckoutLoggedIn() {
        log.info("Proceeding to checkout as authenticated user");
        dismissAdIfPresent();
        click(proceedToCheckoutButton);
        dismissAdIfPresent();
        return new CheckoutPage(driver);
    }

    public LoginPage proceedToCheckoutUnauthenticated() {
        log.info("Proceeding to checkout as guest/unauthenticated user");
        dismissAdIfPresent();
        click(proceedToCheckoutButton);
        waitForVisibility(checkoutModalRegisterLogin);
        click(checkoutModalRegisterLogin);
        dismissAdIfPresent();
        return new LoginPage(driver);
    }

    public CartPage removeFirstItem() {
        log.info("Removing first item from cart");
        dismissAdIfPresent();
        List<WebElement> deletes = driver.findElements(deleteButtons);
        if (!deletes.isEmpty()) {
            deletes.get(0).click();
        }
        return this;
    }
}
