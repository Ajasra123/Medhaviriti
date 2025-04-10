package views;

import controllers.LoginController;
import models.User;
import utils.AnimationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JPanel mainPanel;
    private static final Color PRIMARY_COLOR = new Color(0, 120, 212);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color HOVER_COLOR = new Color(0, 100, 200);
    private static final Color SECONDARY_HOVER_COLOR = new Color(70, 70, 70);

    public LoginFrame() {
        setTitle("Medhaviriti - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main Panel with rounded corners
        mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title with animation
        JLabel titleLabel = new JLabel("Welcome to Medhaviriti") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username Field with modern styling
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = createStyledTextField();
        mainPanel.add(usernameField, gbc);

        // Password Field with modern styling
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = createStyledPasswordField();
        mainPanel.add(passwordField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Login Button with animations
        loginButton = createStyledButton("Login", PRIMARY_COLOR, HOVER_COLOR);
        loginButton.addActionListener(new LoginAction());
        AnimationUtils.addClickAnimation(loginButton);
        buttonPanel.add(loginButton);

        // Register Button with animations
        registerButton = createStyledButton("Create Account", SECONDARY_COLOR, SECONDARY_HOVER_COLOR);
        registerButton.addActionListener(e -> {
            AnimationUtils.addSlideAnimation(mainPanel, mainPanel.getX(), -mainPanel.getWidth(), 300);
            Timer timer = new Timer(300, evt -> {
                new RegisterFrame().setVisible(true);
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        });
        AnimationUtils.addClickAnimation(registerButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Add key listeners for Enter key
        usernameField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> loginButton.doClick());

        // Initial animation
        mainPanel.setLocation(-mainPanel.getWidth(), mainPanel.getY());
        AnimationUtils.addSlideAnimation(mainPanel, -mainPanel.getWidth(), 0, 500);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(33, 33, 33));
        field.setCaretColor(PRIMARY_COLOR);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(33, 33, 33));
        field.setCaretColor(PRIMARY_COLOR);
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AnimationUtils.addHoverAnimation(button, bgColor, hoverColor);
        return button;
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showError("Please enter both username and password.");
                return;
            }

            User user = LoginController.authenticateUser(username, password);
            if (user != null) {
                showSuccess("Login Successful!");
                AnimationUtils.addSlideAnimation(mainPanel, mainPanel.getX(), -mainPanel.getWidth(), 300);
                Timer timer = new Timer(300, evt -> {
                    new EventFrame(user).setVisible(true);
                    dispose();
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                showError("Invalid credentials. Please try again.");
                passwordField.setText("");
                passwordField.requestFocus();
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Failed",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
