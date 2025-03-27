package controllers;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventController {

    // Get all event names
    public static List<String> getAllEventNames() {
        List<String> eventList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT title FROM events";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                eventList.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    // Book an event
    public static boolean bookEvent(int userId, String eventName) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "INSERT INTO bookings (user_id, event_id) VALUES (?, (SELECT id FROM events WHERE name = ?))";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, eventName);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get booked events for a user
    public static List<String> getUserEvents(int userId) {
        List<String> userEvents = new ArrayList<>();
        String query = "SELECT events.title FROM bookings " +  // ✅ Correct column name
                "JOIN events ON bookings.event_id = events.id " +
                "WHERE bookings.user_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userEvents.add(rs.getString("title")); // ✅ Correct column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEvents;
    }

}

