package views;

import models.Event;
import models.Booking;
import controllers.BookingController;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingDialog extends JDialog {
    private Event event;
    private BookingController bookingController;
    private JTextField nameField;
    private JTextField emailField;
    private JSpinner numTicketsSpinner;

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

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Event details section
        JPanel eventDetailsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        eventDetailsPanel.setBorder(BorderFactory.createTitledBorder("Event Details"));
        eventDetailsPanel.add(new JLabel("Event: " + event.getName()));
        eventDetailsPanel.add(new JLabel("Date: " + event.getDate()));
        eventDetailsPanel.add(new JLabel("Time: " + event.getTime()));
        eventDetailsPanel.add(new JLabel("Available Seats: " + 
            (event.getCapacity() - event.getCurrentAttendees())));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(eventDetailsPanel, gbc);

        // Booking form
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Number of Tickets:"), gbc);

        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 
            event.getCapacity() - event.getCurrentAttendees(), 1);
        numTicketsSpinner = new JSpinner(spinnerModel);
        mainPanel.add(numTicketsSpinner, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton bookButton = new JButton("Book Now");
        JButton cancelButton = new JButton("Cancel");

        bookButton.addActionListener(e -> handleBooking());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleBooking() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        int numTickets = (int) numTicketsSpinner.getValue();

        // Validation
        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create booking
        Booking booking = new Booking(
            0, // ID will be set by database
            event.getId(),
            name,
            email,
            numTickets,
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );

        if (bookingController.createBooking(booking)) {
            // Update event attendee count
            for (int i = 0; i < numTickets; i++) {
                event.incrementAttendees();
            }
            
            JOptionPane.showMessageDialog(this,
                "Booking successful!\nA confirmation email will be sent to " + email,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to create booking. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
