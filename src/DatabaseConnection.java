import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db"; // Correct DB URL
    private static final String USERNAME = "root"; // Correct DB username
    private static final String PASSWORD = "GobLiNOvO#03"; // Ensure the password is correct

    public static Connection getConnection() throws SQLException {
        try {
            // Load and register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Return the database connection
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Handle error where the JDBC driver is not found
            throw new SQLException("MySQL JDBC Driver not found. Include the JDBC library in your project.");
        } catch (SQLException e) {
            // Handle general SQL exceptions
            throw new SQLException("Error connecting to the database: " + e.getMessage());
        }
    }
}
