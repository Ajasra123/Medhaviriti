package models;

public class Booking {
    private int id;
    private int eventId;
    private String customerName;
    private String customerEmail;
    private int numTickets;
    private String bookingDate;

    public Booking(int id, int eventId, String customerName, String customerEmail, int numTickets, String bookingDate) {
        this.id = id;
        this.eventId = eventId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.numTickets = numTickets;
        this.bookingDate = bookingDate;
    }

    // Getters
    public int getId() { return id; }
    public int getEventId() { return eventId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public int getNumTickets() { return numTickets; }
    public String getBookingDate() { return bookingDate; }
}