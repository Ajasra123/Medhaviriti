package models;

public class Event {
    private int id;
    private String name, date, location, description;
    private int capacity;
    private int currentAttendees;
    private String time;

    public Event(int id, String name, String date, String time, String location, String description, int capacity, int currentAttendees) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.description = description;
        this.capacity = capacity;
        this.currentAttendees = currentAttendees;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public int getCurrentAttendees() { return currentAttendees; }
    public void incrementAttendees() { this.currentAttendees++; }
    public void decrementAttendees() { if (this.currentAttendees > 0) this.currentAttendees--; }
    public boolean hasAvailableSpace() { return currentAttendees < capacity; }
}
