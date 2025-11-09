package hostel.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "/resources/database.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = DatabaseConfig.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IOException("Configuration file '" + CONFIG_FILE + "' not found in the classpath.");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Warning: Could not load database.properties. Falling back to default MySQL settings. " + e.getMessage());
            // Use default values if config file not found or fails to load
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/hostel_management");
            properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            properties.setProperty("db.user", "root");
            properties.setProperty("db.password", "sandy@123"); // Default password, consider security
        }
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url", "jdbc:mysql://localhost:3306/hostel_management");
    }

    public static String getDbDriver() {
        return properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
    }

    public static String getDbUser() {
        return properties.getProperty("db.user", "root");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password", "");
    }
}
