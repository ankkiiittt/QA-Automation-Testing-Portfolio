@Cart
Feature: Product Catalog and Shopping Cart Operations
  As a shopper on Automation Exercise
  I want to browse products and add items to my shopping cart
  So that I can purchase desired clothing items

  @Smoke @Regression
  Scenario: Add product to shopping cart and view cart items
    Given the user navigates to the Automation Exercise home page
    When the user navigates to the "Products" page
    And searches for product "Dress"
    And adds the first displayed product to the shopping cart
    And clicks on the "View Cart" modal link
    Then the shopping cart page should be displayed
    And the shopping cart should contain at least 1 item
