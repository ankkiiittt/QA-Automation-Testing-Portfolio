package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductsPage;

import java.util.List;

/**
 * UI Test Suite validating Product Catalog browsing and Keyword Search capabilities.
 */
public class ProductSearchTest extends BaseTest {

    @Test(description = "Verify product search returns relevant items matching search query", groups = {"Smoke", "Regression"})
    public void testSearchProductByKeyword() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.clickProducts();

        Assert.assertTrue(productsPage.isProductsPageLoaded(), "Products catalog page should load");

        String keyword = "Dress";
        productsPage.searchProduct(keyword);

        Assert.assertTrue(productsPage.getPageHeaderTitle().toUpperCase().contains("SEARCHED PRODUCTS"),
                "Header title should reflect searched products state");

        List<String> productNames = productsPage.getSearchedProductNames();
        Assert.assertFalse(productNames.isEmpty(), "Search query for '" + keyword + "' should return at least 1 product");

        log.info("Found {} products for keyword '{}'", productNames.size(), keyword);
        for (String name : productNames) {
            Assert.assertTrue(name.toLowerCase().contains(keyword.toLowerCase()),
                    "Product title '" + name + "' should contain search keyword '" + keyword + "'");
        }
    }

    @Test(description = "Verify All Products catalog displays multiple items", groups = {"Regression"})
    public void testAllProductsCatalogNotEmpty() {
        HomePage homePage = new HomePage(driver);
        ProductsPage productsPage = homePage.clickProducts();

        int count = productsPage.getProductCount();
        Assert.assertTrue(count > 0, "Products catalog must display more than 0 items");
        log.info("Total products visible on catalog page: {}", count);
    }
}
