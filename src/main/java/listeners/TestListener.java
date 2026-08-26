package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ConfigReader;
import utils.DBUtils;
import utils.DriverManager;
import utils.ExtentManager;

/**
 * TestNG listener that orchestrates test reporting via ExtentReports
 * and execution result logging via JDBC into MySQL/H2 database.
 */
public class TestListener implements ITestListener, ISuiteListener {
    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        log.info("Starting Test Suite: {}", suite.getName());
        ExtentManager.getExtentReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        log.info("Finished Test Suite: {}", suite.getName());
        ExtentManager.flushReports();
        log.info("Overall Database Pass Rate: {}% across recorded runs in [{}]",
                String.format("%.2f", DBUtils.getOverallPassRate()), DBUtils.getActiveDbType());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        String category = "Regression";
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            category = groups[0];
        } else if (result.getTestClass().getName().contains("api")) {
            category = "API";
        } else if (result.getTestClass().getName().contains("stepdefinitions") || result.getTestClass().getName().contains("runners")) {
            category = "BDD";
        } else {
            category = "UI";
        }

        log.info(">>> STARTING TEST: {} [{}]", testName, category);
        ExtentManager.createTest(testName, description, category);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        String suiteName = result.getTestContext().getSuite().getName();
        String category = getCategory(result);

        log.info("<<< TEST PASSED: {} ({} ms)", testName, duration);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.PASS, MarkupHelper.createLabel("TEST PASSED: " + testName, ExtentColor.GREEN));
        }

        DBUtils.logTestResult(suiteName, testName, category, "PASS", duration, null);
        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        String suiteName = result.getTestContext().getSuite().getName();
        String category = getCategory(result);
        Throwable throwable = result.getThrowable();
        String errorMessage = throwable != null ? throwable.getMessage() : "Unknown Failure";

        log.error("<<< TEST FAILED: {} ({} ms) - Reason: {}", testName, duration, errorMessage);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.FAIL, MarkupHelper.createLabel("TEST FAILED: " + testName, ExtentColor.RED));
            if (throwable != null) {
                test.fail(throwable);
            }

            // Capture screenshot if WebDriver is available for UI tests
            if (ConfigReader.getBoolean("screenshotOnFailure", true)) {
                try {
                    WebDriver driver = DriverManager.getDriver();
                    String base64 = ExtentManager.captureScreenshotBase64(driver);
                    if (base64 != null) {
                        test.fail("Screenshot at point of failure:",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
                    }
                } catch (Exception e) {
                    log.warn("Could not capture screenshot for test {}: {}", testName, e.getMessage());
                }
            }
        }

        DBUtils.logTestResult(suiteName, testName, category, "FAIL", duration, errorMessage);
        ExtentManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        long duration = result.getEndMillis() - result.getStartMillis();
        String testName = result.getMethod().getMethodName();
        String suiteName = result.getTestContext().getSuite().getName();
        String category = getCategory(result);
        String reason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Skipped";

        log.warn("<<< TEST SKIPPED: {} - {}", testName, reason);

        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, MarkupHelper.createLabel("TEST SKIPPED: " + testName, ExtentColor.ORANGE));
            if (result.getThrowable() != null) {
                test.skip(result.getThrowable());
            }
        }

        DBUtils.logTestResult(suiteName, testName, category, "SKIP", duration, reason);
        ExtentManager.removeTest();
    }

    private String getCategory(ITestResult result) {
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) return groups[0];
        if (result.getTestClass().getName().contains("api")) return "API";
        if (result.getTestClass().getName().contains("stepdefinitions") || result.getTestClass().getName().contains("runners")) return "BDD";
        return "UI";
    }
}
