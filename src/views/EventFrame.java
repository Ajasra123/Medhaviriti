package views;

import controllers.EventController;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EventFrame extends JFrame {
    private JComboBox<String> eventDropdown;
    private JButton bookButton, myEventsButton;
    private User loggedInUser;

    public EventFrame(User user) {
        this.loggedInUser = user;

        setTitle("Event Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel for Event Selection
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(220, 220, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Select an Event to Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
        eventDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(eventDropdown, gbc);

        // Book Event Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        bookButton = new JButton("Book Event");
        bookButton.setFont(new Font("Arial", Font.BOLD, 16));
        bookButton.setBackground(new Color(100, 100, 100));
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
        gbc.gridy = 3;
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
        return events.toArray(new String[0]);
    }

    // Book selected event
    private void bookSelectedEvent() {
        String selectedEvent = (String) eventDropdown.getSelectedItem();
        if (selectedEvent != null && loggedInUser != null) {
            boolean success = EventController.bookEvent(loggedInUser.getId(), selectedEvent);
            if (success) {
                JOptionPane.showMessageDialog(this, "Event booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book event!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Show user's booked events
    private void viewMyEvents() {
        List<String> myEvents = EventController.getUserEvents(loggedInUser.getId());
        if (myEvents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No events booked yet!", "My Events", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Your Events:\n" + String.join("\n", myEvents), "My Events", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
