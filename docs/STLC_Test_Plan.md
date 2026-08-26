# Software Testing Life Cycle (STLC) Master Test Plan
**Project:** Automation Exercise E-Commerce Platform  
**Target URL:** `https://automationexercise.com`  
**Framework Type:** Hybrid Test Automation Framework (Selenium POM + REST Assured + Cucumber BDD + TestNG + JDBC)  
**Author:** QA Automation Engineer (Entry-Level / CSE Graduate)  
**Version:** 1.0.0  

---

## 1. Introduction & Objectives
The objective of this test plan is to define the testing strategy, scope, environment, and execution workflow for the Automation Exercise web application and its accompanying REST API. This plan ensures high test coverage, early bug detection across the testing pyramid, and reliable automated regression validation.

```
       / \
      /   \      BDD Acceptance Layer (Cucumber Gherkin Scenarios)
     /  UI \     UI Functional & E2E Layer (Selenium WebDriver + POM)
    /------- \
   /   API    \  REST Assured API Layer (JSON Schema, Status, Payloads)
  /------------\
 /   DATABASE   \ JDBC Execution Logging & Historical Flakiness Analysis
/----------------\
```

---

## 2. Scope of Testing

### 2.1 In-Scope
- **User Authentication:** Login validation (positive credentials, invalid credentials, non-existent user handling), page navigation.
- **Product Catalog & Search:** Product listings, keyword search query validation, product detail views, category filtering.
- **Shopping Cart & Checkout:** Add to cart modals, cart item quantity verification, item deletion, unauthenticated checkout redirection.
- **REST API Endpoints (`/api`):**
  - `GET /api/productsList` & `POST /api/productsList` (negative method validation)
  - `GET /api/brandsList` & `PUT /api/brandsList` (negative method validation)
  - `POST /api/searchProduct` (keyword search & missing parameter negative validation)
- **Database Logging & Reporting:** Real-time test status recording via JDBC, pass-rate calculation, flaky test detection queries.

### 2.2 Out-of-Scope
- Performance and stress load testing under high concurrency.
- Direct credit card banking gateway integration tests (using dummy demo sandbox payment details).
- Mobile native application testing (iOS / Android app stores).

---

## 3. Test Strategy & Methodologies

| Layer | Tools & Technologies | Focus Areas |
|---|---|---|
| **UI Automation** | Selenium WebDriver 4, Java 17+, TestNG, ThreadLocal Driver | Page Object Model (POM), explicit waits, ad/overlay dismissal, cross-browser compatibility |
| **API Automation** | REST Assured 5, JSON Schema Validator, Hamcrest | Schema validation, status code checks, payload contract assertions, negative boundary testing |
| **BDD Acceptance** | Cucumber 7, Gherkin Feature Files | Business-readable behavior specifications, Scenario Outlines with Examples tables |
| **Database Verification** | Java JDBC, MySQL 8.x, H2 In-Memory Fallback | Dynamic test run recording, SQL reporting queries (pass rate, flaky test tracker) |
| **Reporting & Logging** | ExtentReports 5 (Spark), Cucumber HTML, SLF4J | Rich interactive HTML reports, base64 failure screenshots, console logs |
| **CI/CD** | Jenkins (Declarative Pipeline), GitHub Actions | Automated builds on push/PR, headless test execution, artifact archiving |

---

## 4. Entry & Exit Criteria

### 4.1 Entry Criteria
1. Test environment (target site & API endpoints) is accessible and operational.
2. Test automation framework dependencies compiled successfully via Maven.
3. Test data and configuration properties verified in `config.properties`.

### 4.2 Exit Criteria
1. 100% of defined critical smoke and regression test scenarios executed.
2. All blocker/critical severity defects logged and addressed.
3. Test execution reports (ExtentReports and Cucumber HTML) generated and archived.
4. Database test execution log records validated with computed pass-rate.

---

## 5. Defect Management Workflow
```
[Defect Detected] --> [Capture Screenshot & Logs] --> [Log in Issue Tracker]
       |
       v
[Developer Fix]   --> [Re-run Automated Suite]    --> [Close Defect / Verify DB Record]
```

### Defect Severity Matrix:
- **Critical (P1):** Application crash, inability to complete checkout or login.
- **Major (P2):** Product search returns incorrect items, API schema contract broken.
- **Moderate (P3):** UI cosmetic misalignment, missing non-critical modal descriptions.
- **Minor (P4):** Minor typo in prompt messages.

---

## 6. Risk Assessment & Mitigation Plan

| Risk Description | Probability | Impact | Mitigation Strategy |
|---|---|---|---|
| Target site has intermittent third-party Google ads / popups | High | High | Implemented resilient `dismissAdIfPresent()` utility in `BasePage` using JavaScript removal. |
| Network latency causing element load delays | Medium | Medium | Used explicit waits (`WebDriverWait`) rather than hardcoded `Thread.sleep()`. |
| Local developer environment does not have MySQL running | Medium | Low | Implemented automatic JDBC fallback to embedded in-memory H2 database with MySQL compatibility mode. |
