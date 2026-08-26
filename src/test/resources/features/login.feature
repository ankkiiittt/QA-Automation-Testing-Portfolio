@Authentication
Feature: User Login and Authentication Management
  As a registered or prospective user of Automation Exercise
  I want to be able to log into the application with my credentials
  So that I can access personalized shopping and account features

  Background:
    Given the user navigates to the Automation Exercise home page
    When the user clicks on the "Signup / Login" navigation button
    Then the login page should be displayed

  @Smoke @Regression
  Scenario: Navigation to Login Page
    Then the "Login to your account" heading should be visible

  @Regression
  Scenario Outline: Validate login behavior with invalid credentials
    When the user enters email "<email>" and password "<password>"
    And clicks the login button
    Then an error message "<error_message>" should be displayed

    Examples:
      | email                             | password       | error_message                          |
      | invalid_user_12345@domain.com     | wrongpass123   | Your email or password is incorrect!   |
      | non_existing_account@mail.test    | SamplePass99!  | Your email or password is incorrect!   |
      | empty_account_check@test.com      | 12345678       | Your email or password is incorrect!   |
