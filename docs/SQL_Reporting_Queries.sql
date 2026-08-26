-- ============================================================================
-- QA AUTOMATION DATABASE REPORTING QUERIES
-- Database Engine: MySQL 8.x / Compatible with MariaDB and H2 (MySQL Mode)
-- ============================================================================

-- 1. Schema Creation
CREATE TABLE IF NOT EXISTS test_execution_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    test_suite VARCHAR(100) NOT NULL,
    test_name VARCHAR(150) NOT NULL,
    test_category VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    execution_time_ms BIGINT NOT NULL,
    error_message VARCHAR(1000),
    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 2. Analytical Reporting Queries for QA Leads & Dashboards
-- ============================================================================

-- Query 1: Overall Test Pass Rate Percentage
-- Purpose: Provides high-level execution health metric across all suites.
SELECT 
    COUNT(*) AS total_executed,
    SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) AS total_passed,
    SUM(CASE WHEN status = 'FAIL' THEN 1 ELSE 0 END) AS total_failed,
    SUM(CASE WHEN status = 'SKIP' THEN 1 ELSE 0 END) AS total_skipped,
    ROUND((SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) AS pass_percentage
FROM test_execution_logs;


-- Query 2: Flaky Test Detection
-- Purpose: Identifies tests that have both PASS and FAIL results in the history,
-- flagging tests that may be non-deterministic due to timing or environment issues.
SELECT 
    test_name,
    test_category,
    COUNT(DISTINCT status) AS distinct_status_count,
    SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) AS pass_count,
    SUM(CASE WHEN status = 'FAIL' THEN 1 ELSE 0 END) AS fail_count,
    ROUND(SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) AS stability_score_pct
FROM test_execution_logs
GROUP BY test_name, test_category
HAVING COUNT(DISTINCT status) > 1
ORDER BY stability_score_pct ASC;


-- Query 3: Performance & Duration Bottlenecks (Top 5 Slowest Tests)
-- Purpose: Identifies long-running tests for optimization or parallelization.
SELECT 
    test_name,
    test_category,
    AVG(execution_time_ms) AS avg_duration_ms,
    MAX(execution_time_ms) AS max_duration_ms,
    MIN(execution_time_ms) AS min_duration_ms
FROM test_execution_logs
GROUP BY test_name, test_category
ORDER BY avg_duration_ms DESC
LIMIT 5;


-- Query 4: Execution Breakdown by Test Category (UI vs API vs BDD)
-- Purpose: Measures coverage and stability distribution across testing pyramid layers.
SELECT 
    test_category,
    COUNT(*) AS total_runs,
    SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) AS passed,
    SUM(CASE WHEN status = 'FAIL' THEN 1 ELSE 0 END) AS failed,
    ROUND((SUM(CASE WHEN status = 'PASS' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) AS category_pass_rate_pct,
    ROUND(AVG(execution_time_ms), 0) AS avg_duration_ms
FROM test_execution_logs
GROUP BY test_category
ORDER BY total_runs DESC;


-- Query 5: Recent Failures with Error Diagnostic Messages
-- Purpose: Triaging recent broken tests during daily standup / defect triage.
SELECT 
    test_suite,
    test_name,
    test_category,
    error_message,
    execution_time_ms,
    executed_at
FROM test_execution_logs
WHERE status = 'FAIL'
ORDER BY executed_at DESC
LIMIT 10;
