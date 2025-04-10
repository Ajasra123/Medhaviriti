package views;

import controllers.EventController;
import models.User;
import utils.AnimationUtils;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.awt.geom.RoundRectangle2D;

public class EventFrame extends JFrame {
    private JComboBox<String> eventDropdown;
    private JTextField customEventField;
    private JTextField locationField;
    private JFormattedTextField dateField;
    private JSpinner timeSpinner;
    private JCheckBox customEventCheckbox;
    private JButton bookButton, myEventsButton;
    private User loggedInUser;
    private List<Map<String, String>> availableEvents;
    private JPanel mainPanel;
    private static final Color PRIMARY_COLOR = new Color(0, 120, 212);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color HOVER_COLOR = new Color(0, 100, 200);
    private static final Color SECONDARY_HOVER_COLOR = new Color(70, 70, 70);

    public EventFrame(User user) {
        this.loggedInUser = user;

        setTitle("Medhaviriti - Event Booking System");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main Panel with rounded corners
        mainPanel = new JPanel(new BorderLayout(20, 20)) {
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

        // Welcome Panel with animation
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        welcomePanel.setBackground(new Color(245, 245, 250));
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomePanel.add(welcomeLabel);

        // Content Panel with modern styling
        JPanel contentPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        contentPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title with animation
        JLabel titleLabel = new JLabel("Book Your Event") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        // Event Selection with modern styling
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(eventLabel, gbc);

        gbc.gridx = 1;
        eventDropdown = createStyledComboBox();
        refreshEventList();
        contentPanel.add(eventDropdown, gbc);

        // Custom Event Option with modern styling
        gbc.gridx = 0;
        gbc.gridy = 2;
        customEventCheckbox = createStyledCheckBox("Enter Custom Event");
        contentPanel.add(customEventCheckbox, gbc);

        gbc.gridx = 1;
        customEventField = createStyledTextField();
        customEventField.setEnabled(false);
        contentPanel.add(customEventField, gbc);

        // Location Field with modern styling
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        locationField = createStyledTextField();
        contentPanel.add(locationField, gbc);

        // Date Selection with modern styling
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateField = createStyledFormattedTextField(dateFormat);
        dateField.setValue(new Date());
        contentPanel.add(dateField, gbc);

        // Time Selection with modern styling
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel timeLabel = new JLabel("Select Time:");
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(timeLabel, gbc);

        gbc.gridx = 1;
        timeSpinner = createStyledTimeSpinner();
        contentPanel.add(timeSpinner, gbc);

        // Buttons Panel with modern styling
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        buttonPanel.setBackground(new Color(245, 245, 250));

        setupButtons();

        buttonPanel.add(bookButton);
        buttonPanel.add(myEventsButton);

        // Add components to main panel
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Event Listeners
        customEventCheckbox.addActionListener(e -> {
            boolean isCustom = customEventCheckbox.isSelected();
            customEventField.setEnabled(isCustom);
            eventDropdown.setEnabled(!isCustom);
            locationField.setEnabled(isCustom);
            if (!isCustom) {
                updateLocationField();
            }
        });

        eventDropdown.addActionListener(e -> updateLocationField());

        // Initialize location field
        updateLocationField();
        locationField.setEnabled(false);

        // Initial animation
        mainPanel.setLocation(-mainPanel.getWidth(), mainPanel.getY());
        AnimationUtils.addSlideAnimation(mainPanel, -mainPanel.getWidth(), 0, 500);
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<String>() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(33, 33, 33));
        comboBox.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return comboBox;
    }

    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        checkBox.setBackground(new Color(245, 245, 250));
        checkBox.setForeground(new Color(33, 33, 33));
        return checkBox;
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

    private JFormattedTextField createStyledFormattedTextField(SimpleDateFormat format) {
        JFormattedTextField field = new JFormattedTextField(format) {
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

    private JSpinner createStyledTimeSpinner() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        SpinnerDateModel timeModel = new SpinnerDateModel(calendar.getTime(), null, null, Calendar.MINUTE);
        JSpinner spinner = new JSpinner(timeModel) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinner, "hh:mm a");
        spinner.setEditor(timeEditor);
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setBackground(Color.WHITE);
        spinner.setForeground(new Color(33, 33, 33));
        spinner.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return spinner;
    }

    private void setupButtons() {
        // Book Button setup with animations
        bookButton = createStyledButton("Book Event", PRIMARY_COLOR, HOVER_COLOR);
        bookButton.addActionListener(e -> bookSelectedEvent());
        AnimationUtils.addClickAnimation(bookButton);

        // My Events Button setup with animations
        myEventsButton = createStyledButton("My Events", SECONDARY_COLOR, SECONDARY_HOVER_COLOR);
        myEventsButton.addActionListener(e -> {
            AnimationUtils.addSlideAnimation(mainPanel, mainPanel.getX(), -mainPanel.getWidth(), 300);
            Timer timer = new Timer(300, evt -> {
                try {
                    EventManagementFrame frame = new EventManagementFrame(loggedInUser);
                    frame.setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, 
                        "Error opening My Events: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
        AnimationUtils.addClickAnimation(myEventsButton);
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

    private void updateLocationField() {
        if (!customEventCheckbox.isSelected() && eventDropdown.getSelectedIndex() != -1) {
            Map<String, String> selectedEvent = availableEvents.get(eventDropdown.getSelectedIndex());
            locationField.setText(selectedEvent.get("location"));
        }
    }

    private void refreshEventList() {
        availableEvents = EventController.getAllEvents();
        eventDropdown.removeAllItems();
        
        for (Map<String, String> event : availableEvents) {
            eventDropdown.addItem(event.get("name"));
        }
    }

    private void bookSelectedEvent() {
        String selectedDate = dateField.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            // Parse selected date and time
            Date selectedDateTime = new Date();
            Date date = sdf.parse(selectedDate);
            
            // Get selected time
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String selectedTime = timeFormat.format(timeSpinner.getValue());
            Date time = timeFormat.parse(selectedTime);
            
            // Combine date and time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime(time);
            
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            selectedDateTime = calendar.getTime();
            
            // Check if selected date/time is in the future
            Date now = new Date();
            if (selectedDateTime.before(now)) {
                showError("Please select a future date and time.");
                return;
            }
        } catch (ParseException e) {
            showError("Please enter a valid date in YYYY-MM-DD format.");
            return;
        }

        // Get selected time in 24-hour format for database
        SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm");
        String selectedTime = dbTimeFormat.format(timeSpinner.getValue());

        if (customEventCheckbox.isSelected()) {
            String customEventName = customEventField.getText().trim();
            if (customEventName.isEmpty()) {
                showError("Please enter a custom event name.");
                return;
            }

            String location = locationField.getText().trim();
            if (location.isEmpty()) {
                showError("Please enter a location.");
                return;
            }

            try {
                boolean success = EventController.createAndBookEvent(
                    loggedInUser.getId(), 
                    customEventName, 
                    selectedDate,
                    selectedTime,
                    location
                );
                if (success) {
                    showSuccess("Custom event created and booked successfully!");
                    refreshEventList();
                    customEventField.setText("");
                    locationField.setText("");
                    customEventCheckbox.setSelected(false);
                    customEventField.setEnabled(false);
                    locationField.setEnabled(false);
                    eventDropdown.setEnabled(true);
                    updateLocationField();
                } else {
                    showError("Failed to create and book custom event.");
                }
            } catch (Exception e) {
                showError("An error occurred: " + e.getMessage());
            }
        } else {
            int selectedIndex = eventDropdown.getSelectedIndex();
            if (selectedIndex == -1) {
                showError("Please select an event to book.");
                return;
            }

            Map<String, String> selectedEvent = availableEvents.get(selectedIndex);
            String eventName = selectedEvent.get("name");
            
            try {
                boolean success = EventController.bookEvent(
                    loggedInUser.getId(), 
                    eventName, 
                    selectedDate, 
                    selectedTime
                );
                if (success) {
                    showSuccess("Event booked successfully!");
                    refreshEventList();
                } else {
                    showError("Failed to book event. Please try again.");
                }
            } catch (Exception e) {
                showError("An error occurred while booking the event: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
