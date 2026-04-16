package dbnew;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * Handles MySQL database connection for QuickBite app.
 */
public class DBConnection {

    // Database credentials
    private static final String URL      = "jdbc:mysql://localhost:3306/quickbite_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "root";

    /**
     * Returns a new connection to the quickbite_db database.
     * Call this whenever you need to run a query.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
