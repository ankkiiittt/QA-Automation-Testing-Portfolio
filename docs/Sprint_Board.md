# Agile / Scrum Sprint Breakdown & User Story Board

This project is structured according to real-world Agile/Scrum engineering workflows, decomposed into 5 two-week sprints.

```
[Sprint 1: Framework Foundation] ---> [Sprint 2: UI Automation Suite] ---> [Sprint 3: REST API Testing Layer]
                                                                                     |
                                                                                     v
[Sprint 5: CI/CD & STLC Delivery] <--- [Sprint 4: BDD & Database Logger] <-----------+
```

---

## Sprint 1: Framework Architecture & Foundation Setup
**Goal:** Establish project infrastructure, dependency management, driver lifecycle, and configuration patterns.

| Story ID | User Story Title | Estimation (SP) | Acceptance Criteria | Status |
|---|---|---|---|---|
| `US-01` | Maven Build & Dependency Scaffold | 3 SP | `pom.xml` configured with Selenium 4, TestNG, REST Assured, Cucumber 7, ExtentReports 5, and MySQL Connector. | Done |
| `US-02` | Thread-Safe WebDriver Manager | 5 SP | `DriverManager` provides thread-local `WebDriver`, multi-browser support (Chrome, Firefox, Edge), and headless CLI toggles. | Done |
| `US-03` | Configuration Management | 2 SP | `ConfigReader` loads `config.properties` and allows dynamic System property overrides (`-Dheadless=true`). | Done |

---

## Sprint 2: Page Object Model & UI Regression Suite
**Goal:** Implement Page Object Model classes and automated UI regression tests for core user journeys.

| Story ID | User Story Title | Estimation (SP) | Acceptance Criteria | Status |
|---|---|---|---|---|
| `US-04` | BasePage & Core Web Locators | 5 SP | `BasePage` encapsulates explicit waits (`WebDriverWait`), JavaScript clicks, and ad overlay handlers. | Done |
| `US-05` | Authentication Page Objects & Tests | 5 SP | `LoginPage` & `LoginTest` validate positive navigation, invalid credentials error assertions, and signup prompts. | Done |
| `US-06` | Product Catalog & Cart Workflows | 8 SP | `ProductsPage`, `CartPage`, and `CartAndCheckoutTest` automate product search, add-to-cart modal, and checkout routing. | Done |

---

## Sprint 3: REST API Automation Layer
**Goal:** Automate backend service validation against `https://automationexercise.com/api` endpoints using REST Assured.

| Story ID | User Story Title | Estimation (SP) | Acceptance Criteria | Status |
|---|---|---|---|---|
| `US-07` | REST Assured Specifications & Schemas | 3 SP | `BaseApiTest` configures base URI and JSON request specs; JSON schema files drafted in resources. | Done |
| `US-08` | Products & Brands Endpoint Tests | 5 SP | `ProductsApiTest` and `BrandsApiTest` assert status 200, schema match, and 405 negative response codes. | Done |
| `US-09` | Product Search API Validation | 5 SP | `SearchProductApiTest` validates keyword filtering and 400 Bad Request on missing search parameters. | Done |

---

## Sprint 4: BDD Layer & Database Execution Logger
**Goal:** Implement business-readable Gherkin feature files with Cucumber step definitions and JDBC database result logging.

| Story ID | User Story Title | Estimation (SP) | Acceptance Criteria | Status |
|---|---|---|---|---|
| `US-10` | Cucumber BDD Authentication Feature | 5 SP | `login.feature` includes Scenario Outline with Examples table; step definitions mapped to POM methods. | Done |
| `US-11` | Cucumber Cart Management Feature | 5 SP | `cart_management.feature` validates catalog browsing and cart item counts. | Done |
| `US-12` | JDBC Test Result Logger & Reporting | 8 SP | `DBUtils` records test results to MySQL/H2 database; analytical queries compute pass-rate and flaky test tracking. | Done |

---

## Sprint 5: CI/CD Pipeline & Documentation
**Goal:** Build Jenkins and GitHub Actions automated pipelines, master test suite, and STLC documentation.

| Story ID | User Story Title | Estimation (SP) | Acceptance Criteria | Status |
|---|---|---|---|---|
| `US-13` | Master TestNG Suite & Parallel Execution | 3 SP | `testng.xml` ties UI, API, BDD, and DB suites with parallel execution enabled. | Done |
| `US-14` | CI/CD Pipeline Configuration | 5 SP | `Jenkinsfile` and GitHub Actions workflow execute headless tests on push and archive HTML reports. | Done |
| `US-15` | STLC Documentation & Portfolio Delivery | 5 SP | Master STLC test plan, test cases matrix, SQL reporting scripts, and comprehensive README created. | Done |
