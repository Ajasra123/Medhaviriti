package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medhaviriti";
    private static final String USER = "root"; // Change if different
    private static final String PASSWORD = "Ajasra@123"; // Use your MySQL password

    public static Connection connect() {
        Connection conn = null;
        try {
            // ✅ Load MySQL JDBC Driver (Important!)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed! " + e.getMessage());
        }
        return conn;
    }
}
