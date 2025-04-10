package controllers;

import models.Booking;
import models.Event;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingController {
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO bookings (event_id, customer_name, customer_email, num_tickets, booking_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, booking.getEventId());
            stmt.setString(2, booking.getCustomerName());
            stmt.setString(3, booking.getCustomerEmail());
            stmt.setInt(4, booking.getNumTickets());
            stmt.setString(5, booking.getBookingDate());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getBookingsByEventId(int eventId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE event_id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(new Booking(
                    rs.getInt("id"),
                    rs.getInt("event_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_email"),
                    rs.getInt("num_tickets"),
                    rs.getString("booking_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getBookingsByEmail(String email) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer_email = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                bookings.add(new Booking(
                    rs.getInt("id"),
                    rs.getInt("event_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_email"),
                    rs.getInt("num_tickets"),
                    rs.getString("booking_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bookEvent(Event event, Booking booking) {
        // Implement booking logic here
        return createBooking(booking);
    }
}
