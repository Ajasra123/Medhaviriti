package views;

import models.Event;
import models.Booking;
import controllers.BookingController;
import utils.AnimationUtils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.geom.RoundRectangle2D;

public class BookingDialog extends JDialog {
    private Event event;
    private BookingController bookingController;
    private JTextField nameField;
    private JTextField emailField;
    private JSpinner numTicketsSpinner;
    private static final Color PRIMARY_COLOR = new Color(0, 120, 212);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color HOVER_COLOR = new Color(0, 100, 200);
    private static final Color SECONDARY_HOVER_COLOR = new Color(70, 70, 70);

    public BookingDialog(JFrame parent, Event event) {
        super(parent, "Book Event: " + event.getName(), true);
        this.event = event;
        this.bookingController = new BookingController();
        setupUI();
    }

    private void setupUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        // Main panel with rounded corners
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Event details section with modern styling
        JPanel eventDetailsPanel = new JPanel(new GridLayout(4, 1, 5, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        eventDetailsPanel.setBorder(BorderFactory.createTitledBorder("Event Details"));
        eventDetailsPanel.setBackground(new Color(245, 245, 250));
        
        JLabel eventNameLabel = new JLabel("Event: " + event.getName());
        eventNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventDetailsPanel.add(eventNameLabel);

        JLabel dateLabel = new JLabel("Date: " + event.getDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventDetailsPanel.add(dateLabel);

        JLabel timeLabel = new JLabel("Time: " + event.getTime());
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventDetailsPanel.add(timeLabel);

        JLabel seatsLabel = new JLabel("Available Seats: " + 
            (event.getCapacity() - event.getCurrentAttendees()));
        seatsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventDetailsPanel.add(seatsLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(eventDetailsPanel, gbc);

        // Booking form with modern styling
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createStyledTextField();
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createStyledTextField();
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        ticketsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(ticketsLabel, gbc);

        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 
            event.getCapacity() - event.getCurrentAttendees(), 1);
        numTicketsSpinner = createStyledSpinner(spinnerModel);
        mainPanel.add(numTicketsSpinner, gbc);

        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        buttonPanel.setBackground(new Color(245, 245, 250));

        // Back button with animation
        JButton backButton = createStyledButton("Back", SECONDARY_COLOR, SECONDARY_HOVER_COLOR);
        backButton.addActionListener(e -> {
            AnimationUtils.addSlideAnimation(mainPanel, mainPanel.getX(), -mainPanel.getWidth(), 300);
            Timer timer = new Timer(300, evt -> dispose());
            timer.setRepeats(false);
            timer.start();
        });
        AnimationUtils.addClickAnimation(backButton);
        buttonPanel.add(backButton);

        // Book button with animation
        JButton bookButton = createStyledButton("Book Now", PRIMARY_COLOR, HOVER_COLOR);
        bookButton.addActionListener(e -> handleBooking());
        AnimationUtils.addClickAnimation(bookButton);
        buttonPanel.add(bookButton);

        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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

    private JSpinner createStyledSpinner(SpinnerNumberModel model) {
        JSpinner spinner = new JSpinner(model) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setBackground(Color.WHITE);
        spinner.setForeground(new Color(33, 33, 33));
        spinner.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return spinner;
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

    private void handleBooking() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        int numTickets = (Integer) numTicketsSpinner.getValue();

        if (name.isEmpty() || email.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        if (numTickets > (event.getCapacity() - event.getCurrentAttendees())) {
            showError("Not enough seats available");
            return;
        }

        Booking booking = new Booking(0, event.getId(), name, email, numTickets, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        boolean success = bookingController.bookEvent(event, booking);

        if (success) {
            showSuccess("Booking successful!");
            dispose();
        } else {
            showError("Failed to book event. Please try again.");
        }
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
}
