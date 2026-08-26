package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC Database Utility for logging test executions and performing analytical reporting queries.
 * Supports MySQL with graceful automatic fallback to embedded H2 in-memory DB.
 */
public class DBUtils {
    private static final Logger log = LoggerFactory.getLogger(DBUtils.class);
    private static Connection connection;
    private static String activeDbType = "UNKNOWN";

    static {
        initConnection();
        initTable();
    }

    private synchronized static void initConnection() {
        String dbMode = ConfigReader.get("db.mode", "auto").toLowerCase();

        if ("mysql".equals(dbMode) || "auto".equals(dbMode)) {
            String mysqlUrl = ConfigReader.get("db.mysql.url", "jdbc:mysql://localhost:3306/qa_automation");
            String user = ConfigReader.get("db.mysql.username", "root");
            String pass = ConfigReader.get("db.mysql.password", "root");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                DriverManager.setLoginTimeout(3);
                connection = DriverManager.getConnection(mysqlUrl, user, pass);
                activeDbType = "MySQL";
                log.info("Connected to MySQL database at {}", mysqlUrl);
                return;
            } catch (Exception e) {
                log.warn("MySQL connection not available ({}: {}). Falling back to embedded H2 database.",
                        e.getClass().getSimpleName(), e.getMessage());
            }
        }

        // H2 in-memory fallback
        try {
            Class.forName("org.h2.Driver");
            String h2Url = ConfigReader.get("db.h2.url", "jdbc:h2:mem:qa_automation;DB_CLOSE_DELAY=-1;MODE=MySQL");
            String user = ConfigReader.get("db.h2.username", "sa");
            String pass = ConfigReader.get("db.h2.password", "");
            connection = DriverManager.getConnection(h2Url, user, pass);
            activeDbType = "H2 (Embedded In-Memory)";
            log.info("Connected to embedded H2 database ({}) for zero-config test logging", h2Url);
        } catch (Exception e) {
            log.error("Failed to initialize H2 fallback database: {}", e.getMessage(), e);
        }
    }

    private static void initTable() {
        if (connection == null) return;
        String createTableSql = "CREATE TABLE IF NOT EXISTS test_execution_logs ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "test_suite VARCHAR(100), "
                + "test_name VARCHAR(150), "
                + "test_category VARCHAR(50), "
                + "status VARCHAR(20), "
                + "execution_time_ms BIGINT, "
                + "error_message VARCHAR(1000), "
                + "executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSql);
            log.info("Initialized test_execution_logs table in {}", activeDbType);
        } catch (SQLException e) {
            log.error("Error creating test_execution_logs table: {}", e.getMessage(), e);
        }
    }

    /**
     * Records a test execution result to the database table.
     */
    public synchronized static void logTestResult(String suite, String testName, String category,
                                                 String status, long durationMs, String errorMessage) {
        if (connection == null) return;
        String insertSql = "INSERT INTO test_execution_logs (test_suite, test_name, test_category, status, execution_time_ms, error_message) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
            pstmt.setString(1, suite != null ? suite : "DefaultSuite");
            pstmt.setString(2, testName != null ? testName : "UnnamedTest");
            pstmt.setString(3, category != null ? category : "General");
            pstmt.setString(4, status);
            pstmt.setLong(5, durationMs);
            if (errorMessage != null && errorMessage.length() > 950) {
                errorMessage = errorMessage.substring(0, 950) + "...";
            }
            pstmt.setString(6, errorMessage);
            pstmt.executeUpdate();
            log.debug("Logged test [{}] status [{}] to DB ({})", testName, status, activeDbType);
        } catch (SQLException e) {
            log.error("Failed to insert test log into DB: {}", e.getMessage());
        }
    }

    /**
     * Analytical Query: Computes overall test pass rate percentage.
     */
    public synchronized static double getOverallPassRate() {
        if (connection == null) return 0.0;
        String query = "SELECT COUNT(*) AS total, "
                + "SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) AS passed "
                + "FROM test_execution_logs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int total = rs.getInt("total");
                int passed = rs.getInt("passed");
                if (total == 0) return 100.0;
                return (passed * 100.0) / total;
            }
        } catch (SQLException e) {
            log.error("Failed to query overall pass rate: {}", e.getMessage());
        }
        return 0.0;
    }

    /**
     * Analytical Query: Detects flaky tests (tests with both PASS and FAIL statuses recorded).
     */
    public synchronized static List<String> getFlakyTests() {
        List<String> flaky = new ArrayList<>();
        if (connection == null) return flaky;
        String query = "SELECT test_name FROM test_execution_logs "
                + "GROUP BY test_name "
                + "HAVING COUNT(DISTINCT status) > 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                flaky.add(rs.getString("test_name"));
            }
        } catch (SQLException e) {
            log.error("Failed to query flaky tests: {}", e.getMessage());
        }
        return flaky;
    }

    /**
     * Fetches recent execution logs as list of map entries.
     */
    public synchronized static List<Map<String, Object>> getRecentExecutionLogs(int limit) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (connection == null) return list;
        String query = "SELECT test_suite, test_name, test_category, status, execution_time_ms, error_message, executed_at "
                + "FROM test_execution_logs ORDER BY id DESC LIMIT " + limit;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("suite", rs.getString("test_suite"));
                row.put("name", rs.getString("test_name"));
                row.put("category", rs.getString("test_category"));
                row.put("status", rs.getString("status"));
                row.put("time_ms", rs.getLong("execution_time_ms"));
                row.put("error", rs.getString("error_message"));
                row.put("executed_at", rs.getString("executed_at"));
                list.add(row);
            }
        } catch (SQLException e) {
            log.error("Failed to query recent logs: {}", e.getMessage());
        }
        return list;
    }

    public static String getActiveDbType() {
        return activeDbType;
    }

    public synchronized static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
