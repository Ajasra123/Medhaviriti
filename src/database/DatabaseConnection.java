package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medhaviriti";
    private static final String USER = "root"; 
    private static final String PASSWORD = "Ajasra@123"; 

    public static Connection connect() {
        try {
            return getConnection();
        } catch (SQLException e) {
            System.out.println("Database connection failed! " + e.getMessage());
        }
        return null;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
