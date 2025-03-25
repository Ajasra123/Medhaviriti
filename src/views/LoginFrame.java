package views;

import controllers.LoginController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        // Email Field
        gbc.gridx = 1;
        emailField = new JTextField(20);
        add(emailField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        add(loginButton, gbc);

        // Register Button
        gbc.gridy = 3;
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose(); // Close login frame
        });
        add(registerButton, gbc);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Please enter both email and password.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = LoginController.authenticateUser(email, password);
            if (user != null) {
                JOptionPane.showMessageDialog(LoginFrame.this, "Login Successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                new EventFrame().setVisible(true);
                dispose(); // Close login frame
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials. Try again.", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
