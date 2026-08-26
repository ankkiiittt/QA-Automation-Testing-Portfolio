package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread-safe ExtentReports 5 manager for rich HTML test execution reports.
 */
public class ExtentManager {
    private static final Logger log = LoggerFactory.getLogger(ExtentManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    public synchronized static ExtentReports getExtentReports() {
        if (extentReports == null) {
            String reportPath = ConfigReader.get("extentReportPath", "target/extent-reports/ExtentReport.html");
            File reportDir = new File(reportPath).getParentFile();
            if (reportDir != null && !reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle(ConfigReader.get("extentReportTitle", "Automation Test Report"));
            sparkReporter.config().setReportName(ConfigReader.get("extentReportName", "Regression Suite"));
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Environment", "QA / Demo");
            extentReports.setSystemInfo("Application", "Automation Exercise");
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            log.info("Initialized ExtentReports at: {}", reportPath);
        }
        return extentReports;
    }

    public static void createTest(String testName, String description, String category) {
        ExtentTest test = getExtentReports().createTest(testName, description);
        if (category != null && !category.isEmpty()) {
            test.assignCategory(category);
        }
        testThreadLocal.set(test);
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void removeTest() {
        testThreadLocal.remove();
    }

    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            log.info("Flushed ExtentReports to disk");
        }
    }

    /**
     * Capture screenshot as Base64 string for seamless embedding into HTML reports.
     */
    public static String captureScreenshotBase64(WebDriver driver) {
        if (driver == null) {
            return null;
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.error("Failed to capture base64 screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot and save to disk file.
     */
    public static String captureScreenshotFile(WebDriver driver, String screenshotName) {
        if (driver == null) {
            return null;
        }
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            Path dest = Paths.get("target", "screenshots", screenshotName + "_" + timestamp + ".png");
            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest);
            return dest.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("Failed to save screenshot file: {}", e.getMessage());
            return null;
        }
    }
}
