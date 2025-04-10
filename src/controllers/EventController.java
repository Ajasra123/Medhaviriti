package controllers;

import database.DatabaseConnection;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventController {
    public static List<Map<String, String>> getAllEvents() {
        List<Map<String, String>> events = new ArrayList<>();
        String query = "SELECT DISTINCT name, location FROM events";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, String> event = new HashMap<>();
                event.put("name", rs.getString("name"));
                event.put("location", rs.getString("location"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static List<Map<String, String>> getBookedEvents(int userId) {
        List<Map<String, String>> bookedEvents = new ArrayList<>();
        String query = "SELECT b.id, e.name, e.location, b.booking_date, b.booking_time " +
                       "FROM bookings b " +
                       "JOIN events e ON b.event_id = e.id " +
                       "WHERE b.user_id = ? " +
                       "ORDER BY b.booking_date, b.booking_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, String> event = new HashMap<>();
                event.put("id", rs.getString("id"));
                event.put("name", rs.getString("name"));
                event.put("location", rs.getString("location"));
                event.put("date", rs.getString("booking_date"));
                
                // Convert time to AM/PM format
                String time = rs.getString("booking_time");
                try {
                    SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");
                    Date timeDate = sdf24.parse(time);
                    time = sdf12.format(timeDate);
                } catch (Exception e) {
                    // Use original time if parsing fails
                }
                event.put("time", time);
                
                bookedEvents.add(event);
            }
            
            return bookedEvents;
        } catch (SQLException e) {
            e.printStackTrace();
            return bookedEvents;
        }
    }

    public static boolean createAndBookEvent(int userId, String eventName, String date, String time, String location) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Create the event
            String createEventQuery = "INSERT INTO events (name, date, time, location, capacity) VALUES (?, ?, ?, ?, 1)";
            PreparedStatement createEventStmt = conn.prepareStatement(createEventQuery, Statement.RETURN_GENERATED_KEYS);
            createEventStmt.setString(1, eventName);
            createEventStmt.setString(2, date);
            createEventStmt.setString(3, time);
            createEventStmt.setString(4, location);
            createEventStmt.executeUpdate();

            ResultSet rs = createEventStmt.getGeneratedKeys();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int eventId = rs.getInt(1);

            // Book the event
            String bookEventQuery = "INSERT INTO bookings (user_id, event_id, booking_date, booking_time) VALUES (?, ?, ?, ?)";
            PreparedStatement bookEventStmt = conn.prepareStatement(bookEventQuery);
            bookEventStmt.setInt(1, userId);
            bookEventStmt.setInt(2, eventId);
            bookEventStmt.setString(3, date);
            bookEventStmt.setString(4, time);
            bookEventStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean bookEvent(int userId, String eventName, String date, String time) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Create a new event instance for this booking
            String createEventQuery = "INSERT INTO events (name, date, time, location, capacity) " +
                                    "SELECT ?, ?, ?, location, capacity " +
                                    "FROM events WHERE name = ? LIMIT 1";
            PreparedStatement createEventStmt = conn.prepareStatement(createEventQuery, Statement.RETURN_GENERATED_KEYS);
            createEventStmt.setString(1, eventName);
            createEventStmt.setString(2, date);
            createEventStmt.setString(3, time);
            createEventStmt.setString(4, eventName);
            createEventStmt.executeUpdate();

            ResultSet rs = createEventStmt.getGeneratedKeys();
            if (!rs.next()) {
                conn.rollback();
                return false;
            }
            int eventId = rs.getInt(1);

            // Book the event
            String bookEventQuery = "INSERT INTO bookings (user_id, event_id, booking_date, booking_time) VALUES (?, ?, ?, ?)";
            PreparedStatement bookEventStmt = conn.prepareStatement(bookEventQuery);
            bookEventStmt.setInt(1, userId);
            bookEventStmt.setInt(2, eventId);
            bookEventStmt.setString(3, date);
            bookEventStmt.setString(4, time);
            bookEventStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean cancelBooking(int userId, int bookingId) {
        String query = "DELETE FROM bookings WHERE user_id = ? AND id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookingId);
            
            int rowsAffected = stmt.executeUpdate();
            
            // Also delete the event if it's a custom event with no other bookings
            if (rowsAffected > 0) {
                String cleanupQuery = "DELETE FROM events WHERE id IN " +
                                    "(SELECT event_id FROM bookings GROUP BY event_id HAVING COUNT(*) = 0)";
                try (PreparedStatement cleanupStmt = conn.prepareStatement(cleanupQuery)) {
                    cleanupStmt.executeUpdate();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
