package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for Products catalog and Search page.
 */
public class ProductsPage extends BasePage {

    // Locators
    private final By allProductsHeader = By.xpath("//h2[@class='title text-center']");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By productCards = By.xpath("//div[@class='features_items']//div[contains(@class,'col-sm-4')]");
    private final By productNames = By.xpath("//div[@class='features_items']//div[@class='productinfo text-center']//p");
    private final By addToCartButtons = By.xpath("//div[@class='features_items']//div[@class='productinfo text-center']//a[contains(@class,'add-to-cart')]");
    
    // Modal Locators
    private final By modalAddedHeader = By.xpath("//div[@id='cartModal']//h4[contains(text(),'Added!')]");
    private final By continueShoppingButton = By.xpath("//div[@id='cartModal']//button[contains(text(),'Continue Shopping') or @data-dismiss='modal']");
    private final By viewCartModalLink = By.xpath("//div[@id='cartModal']//a[contains(@href,'/view_cart')]");
    private final By viewProductDetailsLinks = By.xpath("//a[contains(@href,'/product_details/')]");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductsPageLoaded() {
        dismissAdIfPresent();
        return isDisplayed(allProductsHeader);
    }

    public String getPageHeaderTitle() {
        dismissAdIfPresent();
        return getText(allProductsHeader);
    }

    public ProductsPage searchProduct(String keyword) {
        log.info("Searching for product with keyword: {}", keyword);
        dismissAdIfPresent();
        type(searchInput, keyword);
        click(searchButton);
        dismissAdIfPresent();
        return this;
    }

    public List<String> getSearchedProductNames() {
        dismissAdIfPresent();
        List<WebElement> elements = driver.findElements(productNames);
        List<String> names = new ArrayList<>();
        for (WebElement el : elements) {
            names.add(el.getText().trim());
        }
        return names;
    }

    public int getProductCount() {
        dismissAdIfPresent();
        return driver.findElements(productCards).size();
    }

    public ProductsPage addProductToCartByIndex(int index) {
        log.info("Adding product at index {} to cart", index);
        dismissAdIfPresent();
        List<WebElement> buttons = driver.findElements(addToCartButtons);
        if (index < buttons.size()) {
            jsClick(buttons.get(index));
        } else {
            throw new IllegalArgumentException("Product index " + index + " out of bounds (total: " + buttons.size() + ")");
        }
        return this;
    }

    public boolean isAddedToCartModalDisplayed() {
        return isDisplayed(modalAddedHeader);
    }

    public ProductsPage clickContinueShopping() {
        log.info("Clicking Continue Shopping in modal");
        click(continueShoppingButton);
        return this;
    }

    public CartPage clickViewCartFromModal() {
        log.info("Clicking View Cart from modal");
        click(viewCartModalLink);
        dismissAdIfPresent();
        return new CartPage(driver);
    }
}
