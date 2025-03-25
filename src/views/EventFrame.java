package views;

import models.Event;
import controllers.EventController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EventFrame extends JFrame {
    private JList<String> eventList;
    private DefaultListModel<String> listModel;
    private JButton bookButton, refreshButton;

    public EventFrame() {
        setTitle("Available Events");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        eventList = new JList<>(listModel);
        add(new JScrollPane(eventList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        bookButton = new JButton("Book Event");
        refreshButton = new JButton("Refresh");

        buttonPanel.add(bookButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEvents(); // Load Events on Start

        // Booking an Event
        bookButton.addActionListener(e -> {
            int selectedIndex = eventList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedEvent = listModel.get(selectedIndex);
                int eventId = EventController.getEventIdByName(selectedEvent);

                if (EventController.bookEvent(1, eventId)) { // Assume user ID = 1 for now
                    JOptionPane.showMessageDialog(null, "Event booked successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Booking failed!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select an event!");
            }
        });

        refreshButton.addActionListener(e -> loadEvents());
        setVisible(true);
    }

    // Load Events from Database
    private void loadEvents() {
        listModel.clear();
        List<Event> events = EventController.getAllEvents();
        for (Event event : events) {
            listModel.addElement(event.getName() + " - " + event.getDate());
        }
    }
}
