package views;

import controllers.EventController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

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

    public EventFrame(User user) {
        this.loggedInUser = user;

        setTitle("Medhaviriti - Event Booking System");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Welcome Panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(new Color(245, 245, 250));
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomePanel.add(welcomeLabel);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Title
        JLabel titleLabel = new JLabel("Book Your Event");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 33, 33));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(titleLabel, gbc);

        // Event Selection
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(eventLabel, gbc);

        gbc.gridx = 1;
        eventDropdown = new JComboBox<>();
        eventDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventDropdown.setBackground(Color.WHITE);
        refreshEventList();
        contentPanel.add(eventDropdown, gbc);

        // Custom Event Option
        gbc.gridx = 0;
        gbc.gridy = 2;
        customEventCheckbox = new JCheckBox("Enter Custom Event");
        customEventCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customEventCheckbox.setBackground(new Color(245, 245, 250));
        contentPanel.add(customEventCheckbox, gbc);

        gbc.gridx = 1;
        customEventField = new JTextField();
        customEventField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customEventField.setEnabled(false);
        contentPanel.add(customEventField, gbc);

        // Location Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        locationField = new JTextField();
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(locationField, gbc);

        // Date Selection
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateField = new JFormattedTextField(dateFormat);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setValue(new Date());
        contentPanel.add(dateField, gbc);

        // Time Selection
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel timeLabel = new JLabel("Select Time:");
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(timeLabel, gbc);

        gbc.gridx = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        SpinnerDateModel timeModel = new SpinnerDateModel(calendar.getTime(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(timeSpinner, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
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
    }

    private void setupButtons() {
        // Book Button setup
        bookButton = new JButton("Book Event");
        styleButton(bookButton, new Color(0, 120, 212));
        bookButton.addActionListener(e -> bookSelectedEvent());

        // My Events Button with fixed functionality
        myEventsButton = new JButton("My Events");
        styleButton(myEventsButton, new Color(51, 51, 51));
        myEventsButton.addActionListener(e -> {
            try {
                System.out.println("My Events button clicked for user: " + loggedInUser.getUsername());
                EventManagementFrame frame = new EventManagementFrame(loggedInUser);
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error opening My Events: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateLocationField() {
        if (!customEventCheckbox.isSelected() && eventDropdown.getSelectedIndex() != -1) {
            Map<String, String> selectedEvent = availableEvents.get(eventDropdown.getSelectedIndex());
            locationField.setText(selectedEvent.get("location"));
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
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
