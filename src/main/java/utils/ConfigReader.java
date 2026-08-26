package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration reader that loads properties from config.properties
 * and supports runtime System property overrides.
 */
public class ConfigReader {
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                log.info("Loaded config.properties successfully");
            } else {
                log.warn("config.properties file not found on classpath, defaulting to system properties");
            }
        } catch (IOException e) {
            log.error("Failed to read config.properties: {}", e.getMessage(), e);
        }
    }

    /**
     * Get property value. Checks System properties first, then config.properties.
     *
     * @param key property key
     * @return property value or null
     */
    public static String get(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        return properties.getProperty(key);
    }

    /**
     * Get property value with a default fallback.
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : defaultValue;
    }

    /**
     * Get integer property value.
     */
    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        if (val != null) {
            try {
                return Integer.parseInt(val.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultValue;
    }

    /**
     * Get boolean property value.
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        if (val != null) {
            return Boolean.parseBoolean(val.trim());
        }
        return defaultValue;
    }
}
