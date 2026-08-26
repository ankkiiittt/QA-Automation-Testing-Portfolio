package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Checkout, Order Review, and Payment flow.
 */
public class CheckoutPage extends BasePage {

    // Checkout Details Locators
    private final By addressDetailsHeader = By.xpath("//h2[contains(text(),'Address Details')]");
    private final By reviewOrderHeader = By.xpath("//h2[contains(text(),'Review Your Order')]");
    private final By orderCommentTextarea = By.name("message");
    private final By placeOrderButton = By.xpath("//a[contains(@href,'/payment') and contains(text(),'Place Order')]");

    // Payment Form Locators
    private final By nameOnCardInput = By.xpath("//input[@data-qa='name-on-card']");
    private final By cardNumberInput = By.xpath("//input[@data-qa='card-number']");
    private final By cvcInput = By.xpath("//input[@data-qa='cvc']");
    private final By expiryMonthInput = By.xpath("//input[@data-qa='expiry-month']");
    private final By expiryYearInput = By.xpath("//input[@data-qa='expiry-year']");
    private final By payAndConfirmButton = By.xpath("//button[@data-qa='pay-button']");

    // Order Success Locators
    private final By orderPlacedHeader = By.xpath("//h2[@data-qa='order-placed']//b[contains(text(),'Order Placed!') or contains(text(),'ORDER PLACED!')]");
    private final By successMessage = By.xpath("//p[contains(text(),'Congratulations! Your order has been confirmed!')]");
    private final By continueButton = By.xpath("//a[@data-qa='continue-button']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isCheckoutPageLoaded() {
        dismissAdIfPresent();
        return isDisplayed(addressDetailsHeader) && isDisplayed(reviewOrderHeader);
    }

    public CheckoutPage enterOrderComment(String comment) {
        log.info("Entering order comment: {}", comment);
        dismissAdIfPresent();
        type(orderCommentTextarea, comment);
        return this;
    }

    public CheckoutPage clickPlaceOrder() {
        log.info("Clicking Place Order button");
        dismissAdIfPresent();
        click(placeOrderButton);
        dismissAdIfPresent();
        return this;
    }

    public CheckoutPage submitPayment(String nameOnCard, String cardNumber, String cvc, String expMonth, String expYear) {
        log.info("Filling payment details for cardholder: {}", nameOnCard);
        dismissAdIfPresent();
        type(nameOnCardInput, nameOnCard);
        type(cardNumberInput, cardNumber);
        type(cvcInput, cvc);
        type(expiryMonthInput, expMonth);
        type(expiryYearInput, expYear);
        click(payAndConfirmButton);
        dismissAdIfPresent();
        return this;
    }

    public boolean isOrderPlacedSuccessfully() {
        dismissAdIfPresent();
        return isDisplayed(orderPlacedHeader) || isDisplayed(successMessage);
    }

    public HomePage clickContinueAfterOrder() {
        log.info("Clicking Continue button after order placement");
        dismissAdIfPresent();
        click(continueButton);
        return new HomePage(driver);
    }
}
