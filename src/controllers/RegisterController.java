package controllers;

import database.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    public static boolean registerUser(String username, String email, String password) {
        Connection conn = DatabaseConnection.connect();

        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registration Successful! Redirecting to Login...", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (SQLException e) {
            String message = e.getMessage();
            if (message.contains("Duplicate entry")) {
                if (message.contains("username")) {
                    JOptionPane.showMessageDialog(null, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (message.contains("email")) {
                    JOptionPane.showMessageDialog(null, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
        return false;
    }
}
