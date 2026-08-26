# Enterprise QA Automation Testing Portfolio Framework

[![Java CI with Maven](https://github.com/qa-portfolio/automation-exercise-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/qa-portfolio/automation-exercise-framework/actions)
[![Selenium WebDriver](https://img.shields.io/badge/Selenium-4.27-brightgreen.svg)](https://www.selenium.dev/)
[![REST Assured](https://img.shields.io/badge/REST_Assured-5.5-orange.svg)](https://rest-assured.io/)
[![Cucumber BDD](https://img.shields.io/badge/Cucumber-7.20-green.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10-red.svg)](https://testng.org/)
[![ExtentReports](https://img.shields.io/badge/ExtentReports-5.1-blue.svg)](https://www.extentreports.com/)

> **Target Role:** Automation Test Engineer / QA Automation Engineer (0–2 Years Experience)  
> **Target Application:** [Automation Exercise](https://automationexercise.com) (E-Commerce Web Application & REST API)  
> **Repository Type:** Production-grade Java Test Automation Framework with POM, REST Assured, Cucumber BDD, JDBC Test Logging, and CI/CD.

---

## 📑 Table of Contents
1. [Executive Summary](#-executive-summary)
2. [Job Description (JD) Skills Mapping Matrix](#-job-description-jd-skills-mapping-matrix)
3. [Framework Architecture & Flow](#-framework-architecture--flow)
4. [Project Structure](#-project-structure)
5. [Prerequisites & Local Setup](#-prerequisites--local-setup)
6. [Test Execution Commands](#-test-execution-commands)
7. [Reporting & Test Diagnostics](#-reporting--test-diagnostics)
8. [Database Test Logging & SQL Analytics](#-database-test-logging--sql-analytics)
9. [CI/CD Integration](#-cicd-integration)
10. [Agile Sprint Breakdown](#-agile-sprint-breakdown)
11. [Interview Talking Points & Future Roadmap](#-interview-talking-points--future-roadmap)

---

## 🎯 Executive Summary
This portfolio project demonstrates hands-on expertise in building an enterprise-ready, maintainable, and scalable test automation framework from scratch. It encompasses full-stack quality engineering:
- **UI Layer:** Selenium WebDriver 4 implementing the Page Object Model (POM) pattern, explicit synchronization, and ThreadLocal thread safety.
- **API Layer:** REST Assured test suite validating JSON Schema contracts, HTTP status codes, and negative boundary responses.
- **BDD Layer:** Cucumber with Gherkin feature files, including Scenario Outlines and data-driven Examples tables.
- **Database Layer:** Real-time test execution logging into MySQL/H2 via JDBC with analytical SQL reporting (pass rate, flaky test tracker).
- **CI/CD Layer:** Multi-stage Declarative Jenkinsfile pipeline and GitHub Actions workflow with automated artifact reporting.

---

## 📋 Job Description (JD) Skills Mapping Matrix

| Required / Preferred Skill | Implementation in Framework | Direct File Reference |
|---|---|---|
| **STLC & SDLC Methodology** | Formal Test Plan, Test Cases Matrix, Defect Lifecycle, and Agile Sprint Plan | [`docs/STLC_Test_Plan.md`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/docs/STLC_Test_Plan.md), [`docs/Test_Cases.md`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/docs/Test_Cases.md) |
| **Selenium WebDriver with POM** | Page Object Model architecture, explicit waits (`WebDriverWait`), dynamic ad dismissal, modular page classes | [`BasePage.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/pages/BasePage.java), [`HomePage.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/pages/HomePage.java), [`ProductsPage.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/pages/ProductsPage.java), [`CartPage.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/pages/CartPage.java) |
| **Thread-Safe Driver Architecture** | `ThreadLocal<WebDriver>` with WebDriverManager and multi-browser support (Chrome, Firefox, Edge, Headless) | [`DriverManager.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/utils/DriverManager.java) |
| **API Testing with REST Assured** | Positive & negative API testing, JSON schema validation, payload body assertions | [`ProductsApiTest.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/api/ProductsApiTest.java), [`BrandsApiTest.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/api/BrandsApiTest.java), [`SearchProductApiTest.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/api/SearchProductApiTest.java) |
| **Test Runner (TestNG)** | Suite orchestration, `@BeforeMethod`/`@AfterMethod`, parallel test execution, assertions, custom listeners | [`testng.xml`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/resources/testng.xml), [`BaseTest.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/tests/BaseTest.java), [`LoginTest.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/tests/LoginTest.java) |
| **Cucumber (BDD)** | Gherkin `.feature` specifications, Scenario Outline with Examples table, Step Definitions, TestNGCucumber runner | [`login.feature`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/resources/features/login.feature), [`LoginSteps.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/stepdefinitions/LoginSteps.java), [`CucumberTestRunner.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/runners/CucumberTestRunner.java) |
| **SQL & Database Concepts** | JDBC result logging into MySQL with zero-config H2 in-memory fallback, analytical pass-rate & flakiness queries | [`DBUtils.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/utils/DBUtils.java), [`DatabaseReportingTest.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/test/java/tests/DatabaseReportingTest.java), [`SQL_Reporting_Queries.sql`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/docs/SQL_Reporting_Queries.sql) |
| **Analytical & Debugging Practices** | ExtentReports 5 integration, automatic Base64 failure screenshot capture, SLF4J logging, meaningful assertion diagnostics | [`ExtentManager.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/utils/ExtentManager.java), [`TestListener.java`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/src/main/java/listeners/TestListener.java) |
| **CI/CD Pipelines** | Jenkins Declarative Pipeline and GitHub Actions workflow with headless browser execution and artifact archiving | [`Jenkinsfile`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/Jenkinsfile), [`.github/workflows/ci.yml`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/.github/workflows/ci.yml) |
| **Git Version Control** | Atomic, descriptive commits aligned with Agile sprint deliverables | Commit History (`git log --oneline --graph`) |

---

## 🏗 Framework Architecture & Flow

```
                      +-----------------------------+
                      |         testng.xml          |
                      | (Master Suite Orchestration)|
                      +--------------+--------------+
                                     |
         +---------------------------+---------------------------+
         |                           |                           |
         v                           v                           v
+------------------+       +-------------------+       +-------------------+
|  REST Assured    |       |  Selenium UI POM  |       |   Cucumber BDD    |
|    API Suite     |       |  Regression Suite |       |    Scenarios      |
+--------+---------+       +---------+---------+       +---------+---------+
         |                           |                           |
         +---------------------------+---------------------------+
                                     |
                                     v
                       +---------------------------+
                       |    TestListener / Hooks   |
                       +-------------+-------------+
                                     |
                   +-----------------+-----------------+
                   |                                   |
                   v                                   v
    +------------------------------+    +------------------------------+
    |    ExtentReports 5 (HTML)    |    |   DBUtils (JDBC MySQL / H2)  |
    |  - Step logs & Status        |    |  - test_execution_logs table |
    |  - Embedded Base64 Snapshots |    |  - Pass rate & Flaky tracker |
    +------------------------------+    +------------------------------+
```

---

## 📂 Project Structure

```
Automation/
├── .github/
│   └── workflows/
│       └── ci.yml                      # GitHub Actions CI/CD workflow
├── docs/
│   ├── STLC_Test_Plan.md               # Master STLC Test Plan & Strategy
│   ├── Test_Cases.md                   # Detailed Test Cases Specification
│   ├── SQL_Reporting_Queries.sql       # Analytical SQL Queries & Table DDL
│   └── Sprint_Board.md                 # Agile User Stories & Sprint Breakdown
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── listeners/
│   │       │   └── TestListener.java   # TestNG ITestListener & ISuiteListener
│   │       ├── pages/                  # Page Object Model (POM) classes
│   │       │   ├── BasePage.java       # Explicit waits, safe click & ad dismissal
│   │       │   ├── HomePage.java       # Navigation & subscription locators
│   │       │   ├── LoginPage.java      # Login & Signup form interactions
│   │       │   ├── ProductsPage.java   # Catalog search & add-to-cart modal
│   │       │   ├── CartPage.java       # Shopping cart operations & table checks
│   │       │   └── CheckoutPage.java   # Address review, payment & confirmation
│   │       └── utils/
│   │           ├── ConfigReader.java   # Singleton property reader & overrides
│   │           ├── DBUtils.java        # JDBC database test logger & SQL queries
│   │           ├── DriverManager.java  # ThreadLocal WebDriver manager
│   │           └── ExtentManager.java  # ExtentReports 5 report & screenshot utility
│   └── test/
│       ├── java/
│       │   ├── api/                    # REST Assured API test suite
│       │   │   ├── BaseApiTest.java
│       │   │   ├── ProductsApiTest.java
│       │   │   ├── BrandsApiTest.java
│       │   │   └── SearchProductApiTest.java
│       │   ├── runners/
│       │   │   └── CucumberTestRunner.java  # TestNG Cucumber Runner
│       │   ├── stepdefinitions/        # Cucumber step definitions
│       │   │   ├── Hooks.java          # Driver setup & failure screenshot hook
│       │   │   ├── LoginSteps.java
│       │   │   └── CartSteps.java
│       │   └── tests/                  # TestNG UI regression test suite
│       │       ├── BaseTest.java
│       │       ├── LoginTest.java
│       │       ├── ProductSearchTest.java
│       │       ├── CartAndCheckoutTest.java
│       │       └── DatabaseReportingTest.java
│       └── resources/
│           ├── config.properties       # Environment and browser configurations
│           ├── features/               # Gherkin BDD feature files
│           │   ├── login.feature
│           │   └── cart_management.feature
│           ├── schemas/                # JSON Schema validation contracts
│           │   ├── products_schema.json
│           │   └── brands_schema.json
│           └── testng.xml              # Master TestNG XML suite runner
├── Jenkinsfile                         # Declarative Jenkins CI/CD pipeline
├── pom.xml                             # Maven build dependencies & plugins
├── .gitignore
└── README.md
```

---

## ⚙️ Prerequisites & Local Setup

### 1. Requirements
- **JDK:** Java 17 or higher (`java -version`)
- **Maven:** Apache Maven 3.8+ (`mvn -version`)
- **Browser:** Google Chrome (installed locally)
- **Database (Optional):** MySQL 8.x running locally or via Docker:
  ```bash
  docker run -d --name mysql-qa -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=qa_automation mysql:8.0
  ```
  *(Note: If MySQL is not running, the framework automatically activates an embedded in-memory H2 database in MySQL mode, allowing full test execution out of the box with zero external dependencies!)*

---

## 🚀 Test Execution Commands

### Run Full Master Suite (UI, API, BDD, DB) in Headless Mode:
```bash
mvn clean test -Dheadless=true
```

### Run Full Suite in Headed Mode (Opens Browser Windows):
```bash
mvn clean test -Dheadless=false
```

### Run Only API Test Suite:
```bash
mvn test -Dtest=ProductsApiTest,BrandsApiTest,SearchProductApiTest
```

### Run Only Cucumber BDD Suite:
```bash
mvn test -Dtest=CucumberTestRunner -Dheadless=true
```

### Run Specific Test Class (e.g. Login Tests):
```bash
mvn test -Dtest=LoginTest -Dheadless=true
```

### Run via TestNG XML Suite:
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

---

## 📊 Reporting & Test Diagnostics

### 1. ExtentReports 5 (Spark HTML)
Located at: `target/extent-reports/ExtentReport.html`
- Color-coded pass/fail/skip status badges.
- Category breakdowns (UI, API, BDD, Database).
- Base64 inline failure screenshots and stack trace diagnostics.

### 2. Cucumber BDD HTML Report
Located at: `target/cucumber-reports/cucumber-report.html`
- Step-by-step Gherkin execution breakdown.
- Scenario Outline data table iterations.

---

## 🗄 Database Test Logging & SQL Analytics

All test runs are dynamically recorded to the `test_execution_logs` database table via JDBC in `TestListener.java`.

### Sample Analytical Queries from [`docs/SQL_Reporting_Queries.sql`](file:///c:/Users/Asus/OneDrive/Desktop/Automation/docs/SQL_Reporting_Queries.sql):

```sql
-- 1. Calculate Overall Test Pass Rate Percentage
SELECT 
    COUNT(*) AS total_executed,
    SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) AS total_passed,
    SUM(CASE WHEN status = 'FAIL' THEN 1 ELSE 0 END) AS total_failed,
    ROUND((SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) AS pass_percentage
FROM test_execution_logs;

-- 2. Detect Flaky Tests (Tests with alternating PASS and FAIL outcomes)
SELECT 
    test_name,
    test_category,
    COUNT(DISTINCT status) AS distinct_status_count,
    ROUND(SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) AS stability_score_pct
FROM test_execution_logs
GROUP BY test_name, test_category
HAVING COUNT(DISTINCT status) > 1;
```

---

## 🔄 CI/CD Integration

### Jenkins Declarative Pipeline (`Jenkinsfile`)
- **Stage 1: Checkout** — Git SCM retrieval.
- **Stage 2: Compile & Validate** — `mvn clean test-compile`.
- **Stage 3: Execute Test Suites** — `mvn test -Dheadless=true`.
- **Stage 4: Publish Reports** — Archives Extent Reports, Cucumber Reports, and JUnit XML.

### GitHub Actions (`.github/workflows/ci.yml`)
- Triggers on every `push` and `pull_request` to `master`/`main`.
- Proactively spins up a MySQL container service.
- Executes headless tests and uploads build artifacts for 14-day retention.

---

## 🏃 Agile Sprint Breakdown

| Sprint | Focus Area | Deliverables |
|---|---|---|
| **Sprint 1** | Framework Foundation | Maven scaffold, `pom.xml`, `DriverManager` (ThreadLocal), `ConfigReader` |
| **Sprint 2** | UI Regression Suite | `BasePage`, `HomePage`, `LoginPage`, `ProductsPage`, `CartPage`, `LoginTest`, `ProductSearchTest` |
| **Sprint 3** | REST API Suite | `BaseApiTest`, `ProductsApiTest`, `BrandsApiTest`, `SearchProductApiTest`, JSON Schemas |
| **Sprint 4** | BDD & Database Logger | `login.feature`, `cart_management.feature`, Step Definitions, `DBUtils` JDBC logger |
| **Sprint 5** | CI/CD & STLC Delivery | `testng.xml`, `Jenkinsfile`, `.github/workflows/ci.yml`, Test Plan, Test Cases, README |

---

## 💡 Interview Talking Points & Future Roadmap

When discussing this project in technical interviews, highlight these architectural decisions and planned enhancements:

1. **Anti-Flakiness Strategy:** Explain how `BasePage.dismissAdIfPresent()` uses JavaScript DOM evaluation to strip unpredictable third-party Google ads, and how explicit dynamic waits prevent race conditions without `Thread.sleep()`.
2. **Database Fallback Pattern:** Discuss why you built the dual-mode JDBC utility (`DBUtils`): it enables seamless execution across local developer laptops (via H2 in-memory) while connecting to full MySQL instances in CI/CD containers.
3. **Thread Safety & Parallel Execution:** Explain how `ThreadLocal<WebDriver>` ensures that concurrent TestNG threads never collide or share browser instances.
4. **Planned Roadmap / Next Steps:**
   - **Cross-Browser Cloud Grid:** Integrate Selenium Grid / BrowserStack / SauceLabs for cloud cross-browser testing across mobile viewports.
   - **Allure Reporting Integration:** Add `@Epic`, `@Feature`, `@Story`, and `@Severity` annotations with Allure report lifecycle adapters.
   - **DataProvider Negative Matrix:** Expand negative authentication testing with TestNG `@DataProvider` driving external CSV/Excel data files via Apache POI.
   - **Performance Testing:** Integrate k6 or Apache JMeter scripts for API load benchmarking on `/api/productsList`.
