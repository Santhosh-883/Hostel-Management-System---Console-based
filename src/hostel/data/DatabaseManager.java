package hostel.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUser(), DatabaseConfig.getDbPassword());
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUser(), DatabaseConfig.getDbPassword());
            createTables();
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "roll_number VARCHAR(50) PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "room_number TEXT NOT NULL," +
                    "phone_number TEXT NOT NULL" +
                    ")");

            // Wardens table
            stmt.execute("CREATE TABLE IF NOT EXISTS wardens (" +
                    "username VARCHAR(50) PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")");

            // Complaints table
            stmt.execute("CREATE TABLE IF NOT EXISTS complaints (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "student_roll_number VARCHAR(50) NOT NULL," +
                    "description TEXT NOT NULL," +
                    "created_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (student_roll_number) REFERENCES students(roll_number)" +
                    ")");

            // Leave requests table
            stmt.execute("CREATE TABLE IF NOT EXISTS leave_requests (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "student_roll_number VARCHAR(50) NOT NULL," +
                    "reason TEXT NOT NULL," +
                    "from_date TEXT NOT NULL," +
                    "to_date TEXT," +
                    "hour TEXT NOT NULL," +
                    "status VARCHAR(20) DEFAULT 'Pending'," +
                    "created_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (student_roll_number) REFERENCES students(roll_number)" +
                    ")");

            // Attendance table
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "student_roll_number VARCHAR(50) NOT NULL," +
                    "date TEXT NOT NULL," +
                    "in_time TEXT NOT NULL," +
                    "out_time VARCHAR(20) DEFAULT 'Not Marked'," +
                    "FOREIGN KEY (student_roll_number) REFERENCES students(roll_number)" +
                    ")");

            // Polls table
            stmt.execute("CREATE TABLE IF NOT EXISTS polls (" +
                    "poll_id VARCHAR(255) PRIMARY KEY," +
                    "title TEXT NOT NULL," +
                    "question TEXT NOT NULL," +
                    "options TEXT NOT NULL," +
                    "creator_warden VARCHAR(50) NOT NULL," +
                    "created_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (creator_warden) REFERENCES wardens(username)" +
                    ")");

            // Votes table
            stmt.execute("CREATE TABLE IF NOT EXISTS votes (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "poll_id VARCHAR(255) NOT NULL," +
                    "student_roll_number VARCHAR(50) NOT NULL," +
                    "selected_option TEXT NOT NULL," +
                    "vote_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (poll_id) REFERENCES polls(poll_id)," +
                    "FOREIGN KEY (student_roll_number) REFERENCES students(roll_number)," +
                    "UNIQUE(poll_id, student_roll_number)" +
                    ")");

            // Notifications table
            stmt.execute("CREATE TABLE IF NOT EXISTS notifications (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "recipient_roll_number VARCHAR(50) NOT NULL," +
                    "message TEXT NOT NULL," +
                    "timestamp TEXT NOT NULL," +
                    "`read` INTEGER DEFAULT 0," +
                    "FOREIGN KEY (recipient_roll_number) REFERENCES students(roll_number)" +
                    ")");

            // Remember me table
            stmt.execute("CREATE TABLE IF NOT EXISTS remember_me (" +
                    "user_type VARCHAR(50) PRIMARY KEY," +
                    "identifier TEXT NOT NULL" +
                    ")");

            // Insert default warden if not exists
            stmt.execute("INSERT IGNORE INTO wardens (username, name, password) VALUES ('admin', 'Admin Warden', 'password')");

        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
