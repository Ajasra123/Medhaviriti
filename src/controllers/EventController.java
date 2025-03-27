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
    public static boolean bookEvent(int userId, String eventName, String eventDate) {
        boolean success = false;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM events WHERE title = ?")) {
            stmt.setString(1, eventName);
            ResultSet rs = stmt.executeQuery();

            int eventId = -1;
            if (rs.next()) {
                eventId = rs.getInt("id");
            } else {
                // Insert new event with the provided date
                try (PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO events (title, date) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, eventName);
                    insertStmt.setString(2, eventDate);
                    insertStmt.executeUpdate();
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        eventId = generatedKeys.getInt(1);
                    }
                }
            }

            // Now book the event
            if (eventId != -1) {
                try (PreparedStatement bookStmt = conn.prepareStatement(
                        "INSERT INTO bookings (user_id, event_id) VALUES (?, ?)")) {
                    bookStmt.setInt(1, userId);
                    bookStmt.setInt(2, eventId);
                    bookStmt.executeUpdate();
                    success = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }



    // Get booked events for a user
    public static List<String[]> getUserEvents(int userId) {
        List<String[]> userEvents = new ArrayList<>();
        String query = "SELECT events.title, events.date FROM bookings " +
                "JOIN events ON bookings.event_id = events.id " +
                "WHERE bookings.user_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String eventTitle = rs.getString("title");
                String eventDate = rs.getString("date");
                userEvents.add(new String[]{eventTitle, eventDate});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userEvents;
    }

}

