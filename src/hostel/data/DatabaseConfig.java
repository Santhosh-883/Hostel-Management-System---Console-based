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
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            // Use default values if config file not found
            properties.setProperty("db.url", "jdbc:sqlite:data/hostel_management.db");
            properties.setProperty("db.driver", "org.sqlite.JDBC");
        }
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url", "jdbc:sqlite:data/hostel_management.db");
    }

    public static String getDbDriver() {
        return properties.getProperty("db.driver", "org.sqlite.JDBC");
    }
}
