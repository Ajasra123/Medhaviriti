package controllers;

import database.DatabaseConnection;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginController {
    public static User authenticateUser(String username, String password) {
        System.out.println("\nAttempting login with username: " + username);
        System.out.println("Password length: " + password.length());

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.connect();
            if (conn == null) {
                System.out.println("Database connection failed");
                return null;
            }
            System.out.println("Database connected successfully");

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            System.out.println("Executing query for user: " + username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String dbUsername = rs.getString("username");
                String dbPassword = rs.getString("password");
                String email = rs.getString("email");

                System.out.println("User found in database:");
                System.out.println("ID: " + id);
                System.out.println("Username: " + dbUsername);
                System.out.println("Password match: " + dbPassword.equals(password));
                System.out.println("Email: " + email);

                User user = new User(id, dbUsername, dbPassword, email);
                System.out.println("User object created successfully");
                return user;
            } else {
                System.out.println("No user found with these credentials");
                System.out.println("Checking if user exists...");
                
                // Check if username exists
                sql = "SELECT COUNT(*) FROM users WHERE username = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                rs = stmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Username exists but password is incorrect");
                } else {
                    System.out.println("Username does not exist");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }
}
