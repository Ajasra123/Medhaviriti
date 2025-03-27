package views;

import controllers.EventController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventFrame extends JFrame {
    private JComboBox<String> eventDropdown;
    private JTextField customEventField;
    private JFormattedTextField dateField;
    private JCheckBox customEventCheckbox;
    private JButton bookButton, myEventsButton;
    private User loggedInUser;

    public EventFrame(User user) {
        this.loggedInUser = user;

        setTitle("Medhaviriti");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel for Event Selection
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Select an Event to Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Event Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Event:"), gbc);

        // Dropdown with event names
        gbc.gridx = 1;
        eventDropdown = new JComboBox<>(getEventNames());
        eventDropdown.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(eventDropdown, gbc);

        // Custom Event Checkbox
        gbc.gridx = 0;
        gbc.gridy = 2;
        customEventCheckbox = new JCheckBox("Enter Custom Event");
        panel.add(customEventCheckbox, gbc);

        // Custom Event TextField
        gbc.gridx = 1;
        customEventField = new JTextField(15);
        customEventField.setEnabled(false);
        panel.add(customEventField, gbc);

        // Enable text field when checkbox is selected
        customEventCheckbox.addActionListener(e -> {
            customEventField.setEnabled(customEventCheckbox.isSelected());
            eventDropdown.setEnabled(!customEventCheckbox.isSelected());
        });

        // Date Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);

        // Date Input Field (Formatted)
        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateField = new JFormattedTextField(dateFormat);
        dateField.setColumns(10);
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dateField.setValue(new Date()); // Default to today’s date
        panel.add(dateField, gbc);

        // Book Event Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        bookButton = new JButton("Book Event");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(0, 123, 255));
        bookButton.setForeground(Color.WHITE);
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookSelectedEvent();
            }
        });
        panel.add(bookButton, gbc);

        // My Events Button
        gbc.gridy = 5;
        myEventsButton = new JButton("My Events");
        myEventsButton.setFont(new Font("Arial", Font.BOLD, 16));
        myEventsButton.setBackground(Color.BLACK);
        myEventsButton.setForeground(Color.WHITE);
        myEventsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        myEventsButton.addActionListener(e -> viewMyEvents());
        panel.add(myEventsButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    // Fetch events from the database
    private String[] getEventNames() {
        List<String> events = EventController.getAllEventNames();
        return events.isEmpty() ? new String[]{"No Events Available"} : events.toArray(new String[0]);
    }

    // Book selected event
    private void bookSelectedEvent() {
        String selectedEvent = customEventCheckbox.isSelected() ? customEventField.getText() : (String) eventDropdown.getSelectedItem();
        String selectedDate = dateField.getText();

        // Validate event name
        if (selectedEvent == null || selectedEvent.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an event name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(selectedDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (loggedInUser != null) {
            boolean success = EventController.bookEvent(loggedInUser.getId(), selectedEvent, selectedDate);
            if (success) {
                JOptionPane.showMessageDialog(this, "Event booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book event!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Show user's booked events
    // Show user's booked events with full details
    private void viewMyEvents() {
        List<String[]> myEvents = EventController.getUserEvents(loggedInUser.getId());

        if (myEvents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No events booked yet!", "My Events", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder eventDetails = new StringBuilder("Your Events:\n\n");
            for (String[] event : myEvents) {
                eventDetails.append("📌 Event: ").append(event[0])
                        .append("\n📅 Date: ").append(event[1])
                        .append("\n──────────────────────\n");
            }
            JOptionPane.showMessageDialog(this, eventDetails.toString(), "My Events", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
