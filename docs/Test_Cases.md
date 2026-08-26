# Comprehensive Test Case Specification Catalog

**Application Under Test:** Automation Exercise (`https://automationexercise.com`)  
**Total Automated Cases:** 15 Scenarios across UI, API, BDD, and Database Layers  

---

## 1. UI Test Cases (Selenium WebDriver + TestNG)

| Test Case ID | Module / Feature | Description | Preconditions | Test Steps | Expected Result | Severity | Automation Class / Method |
|---|---|---|---|---|---|---|---|
| `TC-UI-001` | Authentication | Validate login with invalid password | User on Home Page | 1. Click Signup/Login<br>2. Enter email & wrong password<br>3. Click Login | Error prompt `Your email or password is incorrect!` displayed | High | `LoginTest.testLoginWithInvalidPassword()` |
| `TC-UI-002` | Authentication | Validate signup with existing email | User on Home Page | 1. Click Signup/Login<br>2. Enter existing name & email<br>3. Click Signup | Duplicate account warning shown or registration handled | Medium | `LoginTest.testSignupWithExistingEmail()` |
| `TC-UI-003` | Navigation | Verify navigation to Login page | User on Home Page | 1. Click Signup/Login nav | URL contains `/login` and header `Login to your account` visible | High | `LoginTest.testNavigationToLoginPage()` |
| `TC-UI-004` | Catalog / Search | Search product by keyword | User on Home Page | 1. Click Products<br>2. Search "Dress"<br>3. Verify results | "SEARCHED PRODUCTS" header visible, returned products contain keyword | High | `ProductSearchTest.testSearchProductByKeyword()` |
| `TC-UI-005` | Catalog | Verify product catalog not empty | User on Home Page | 1. Click Products<br>2. Count product cards | Total product count > 0 | High | `ProductSearchTest.testAllProductsCatalogNotEmpty()` |
| `TC-UI-006` | Cart | Add product to cart & verify modal | User on Products Page | 1. Click Add to Cart on 1st product<br>2. Click View Cart in modal | Modal confirms addition, Cart page shows item count >= 1 | High | `CartAndCheckoutTest.testAddProductToCartAndVerify()` |
| `TC-UI-007` | Checkout | Guest checkout prompts login | Product in Cart | 1. Go to Cart<br>2. Click Proceed to Checkout<br>3. Click Register/Login in modal | User is redirected to `/login` page | High | `CartAndCheckoutTest.testProceedToCheckoutAsGuestPromptsLogin()` |

---

## 2. API Test Cases (REST Assured)

| Test Case ID | Endpoint | Method | Type | Description | Expected Status & Body | Severity | Automation Class / Method |
|---|---|---|---|---|---|---|---|
| `TC-API-001` | `/api/productsList` | `GET` | Positive | Retrieve all products list | Status 200, `responseCode: 200`, non-empty products array, adheres to JSON schema | Critical | `ProductsApiTest.testGetAllProductsListSuccess()` |
| `TC-API-002` | `/api/productsList` | `POST` | Negative | Unsupported POST method to products list | `responseCode: 405`, `message: "This request method is not supported."` | Medium | `ProductsApiTest.testPostToAllProductsListNotSupported()` |
| `TC-API-003` | `/api/brandsList` | `GET` | Positive | Retrieve all brands list | Status 200, `responseCode: 200`, non-empty brands array with `id` and `brand` | High | `BrandsApiTest.testGetAllBrandsListSuccess()` |
| `TC-API-004` | `/api/brandsList` | `PUT` | Negative | Unsupported PUT method to brands list | `responseCode: 405`, `message: "This request method is not supported."` | Medium | `BrandsApiTest.testPutToAllBrandsListNotSupported()` |
| `TC-API-005` | `/api/searchProduct` | `POST` | Positive | Search product with parameter `search_product=top` | Status 200, `responseCode: 200`, products matching search term returned | High | `SearchProductApiTest.testSearchProductWithValidQuery()` |
| `TC-API-006` | `/api/searchProduct` | `POST` | Negative | Search product without parameter | `responseCode: 400`, `message: "Bad request, search_product parameter is missing in POST request."` | High | `SearchProductApiTest.testSearchProductWithoutParameterReturns400()` |

---

## 3. BDD Acceptance Scenarios (Cucumber Gherkin)

| Test Case ID | Feature File | Scenario Type | Description | Key Gherkin Steps | Automation Runner |
|---|---|---|---|---|---|
| `TC-BDD-001` | `login.feature` | Scenario | Navigate to Login page | `Given user navigates to home page ... Then heading should be visible` | `CucumberTestRunner` |
| `TC-BDD-002` | `login.feature` | Scenario Outline | Data-driven invalid login validation | `When user enters "<email>" and "<password>" ... Then error "<error_message>" displayed` | `CucumberTestRunner` |
| `TC-BDD-003` | `cart_management.feature` | Scenario | Search product & add to cart | `When user searches "Dress" and adds to cart ... Then cart contains at least 1 item` | `CucumberTestRunner` |

---

## 4. Database Reporting & Diagnostics

| Test Case ID | Feature | Description | SQL / Method | Expected Result | Automation Method |
|---|---|---|---|---|---|
| `TC-DB-001` | DB Connection | Verify JDBC connection to MySQL / H2 | `DBUtils.getActiveDbType()` | Active DB type initialized and not null | `DatabaseReportingTest.testDatabaseConnectivity()` |
| `TC-DB-002` | Result Logging | Verify insertion of test execution logs | `DBUtils.logTestResult()` | Record queryable in `test_execution_logs` | `DatabaseReportingTest.testLogTestResultAndQuery()` |
| `TC-DB-003` | Analytics | Verify pass-rate and flaky test queries | `DBUtils.getOverallPassRate()`, `DBUtils.getFlakyTests()` | Pass rate is 0-100%, flaky test detected | `DatabaseReportingTest.testAnalyticalQueriesExecution()` |
