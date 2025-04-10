package views;

import controllers.RegisterController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterFrame() {
        setTitle("Medhaviriti - Create Account");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = createTextField();
        mainPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createTextField();
        mainPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = createPasswordField();
        mainPanel.add(passwordField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new JButton("Create Account");
        styleButton(registerButton);
        mainPanel.add(registerButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    showError("All fields are required!");
                    return;
                }

                if (!isValidEmail(email)) {
                    showError("Please enter a valid email address!");
                    return;
                }

                if (password.length() < 6) {
                    showError("Password must be at least 6 characters long!");
                    return;
                }

                boolean success = RegisterController.registerUser(username, email, password);
                if (success) {
                    showSuccess("Registration Successful! Redirecting to Login...");
                    dispose();
                    new LoginFrame().setVisible(true);
                }
            }
        });

        // Add key listeners for Enter key
        usernameField.addActionListener(e -> emailField.requestFocus());
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> registerButton.doClick());

        add(mainPanel);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 212));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 212));
            }
        });
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return passwordField;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
