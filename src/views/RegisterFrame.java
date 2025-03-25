package views;

import controllers.RegisterController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterFrame() {
        setTitle("Register");
        setSize(300, 200); // Compact window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4); // Spacing

        // Labels and Input Fields
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = createTextField();
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = createTextField();
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = createPasswordField();
        mainPanel.add(passwordField, gbc);

        // Register Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(100, 25)); // Small button
        registerButton.setFocusPainted(false);
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(Color.WHITE);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(registerButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = RegisterController.registerUser(name, email, password);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Registration Successful! Redirecting to Login...", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the registration window

                    // ✅ OPEN LOGIN FRAME
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(10); // Small width
        textField.setPreferredSize(new Dimension(140, 20)); // Small height
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(10); // Small width
        passwordField.setPreferredSize(new Dimension(140, 20)); // Small height
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        return passwordField;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
