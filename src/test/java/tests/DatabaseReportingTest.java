package tests;

import listeners.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * Database Verification & Reporting Test Suite.
 * Validates JDBC logging, pass-rate calculation, and flaky test detection queries.
 */
@Listeners(TestListener.class)
public class DatabaseReportingTest {
    private static final Logger log = LoggerFactory.getLogger(DatabaseReportingTest.class);

    @Test(description = "Verify JDBC database connectivity and table schema initialization", groups = {"Database"})
    public void testDatabaseConnectivity() {
        String activeDb = DBUtils.getActiveDbType();
        log.info("Active Database Engine for test logging: {}", activeDb);
        Assert.assertNotNull(activeDb, "Active database type should not be null");
        Assert.assertTrue(activeDb.contains("MySQL") || activeDb.contains("H2"),
                "Active DB should be either MySQL or embedded H2 fallback");
    }

    @Test(description = "Verify test execution log insertion and retrieval via JDBC", groups = {"Database"})
    public void testLogTestResultAndQuery() {
        String testSuite = "DatabaseVerificationSuite";
        String testName = "testSampleLoggingVerification";
        String category = "Database";
        long duration = 250L;

        DBUtils.logTestResult(testSuite, testName, category, "PASS", duration, null);

        List<Map<String, Object>> logs = DBUtils.getRecentExecutionLogs(10);
        Assert.assertFalse(logs.isEmpty(), "Execution logs list should contain recorded entries");

        log.info("Successfully verified JDBC insertion. Total recent logs fetched: {}", logs.size());
    }

    @Test(description = "Verify analytical queries for pass rate and flaky test detection", groups = {"Database"})
    public void testAnalyticalQueriesExecution() {
        // Record test scenarios to simulate metrics
        DBUtils.logTestResult("RegressionSuite", "testPaymentModule", "UI", "PASS", 1200L, null);
        DBUtils.logTestResult("RegressionSuite", "testPaymentModule", "UI", "FAIL", 850L, "Timeout waiting for payment gateway");
        DBUtils.logTestResult("RegressionSuite", "testSearchApi", "API", "PASS", 320L, null);

        double passRate = DBUtils.getOverallPassRate();
        log.info("Calculated Overall Pass Rate: {}%", String.format("%.2f", passRate));
        Assert.assertTrue(passRate >= 0.0 && passRate <= 100.0, "Pass rate must be a valid percentage between 0 and 100");

        List<String> flakyTests = DBUtils.getFlakyTests();
        log.info("Detected Flaky Tests across recorded runs: {}", flakyTests);
        Assert.assertNotNull(flakyTests, "Flaky tests list must not be null");
        Assert.assertTrue(flakyTests.contains("testPaymentModule"),
                "Flaky test detection query should flag 'testPaymentModule' due to alternating PASS/FAIL statuses");
    }
}
