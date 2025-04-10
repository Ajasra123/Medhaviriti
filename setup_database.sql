DROP DATABASE IF EXISTS medhaviriti;
CREATE DATABASE medhaviriti;
USE medhaviriti;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    location VARCHAR(200) NOT NULL,
    capacity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    booking_date DATE NOT NULL,
    booking_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    UNIQUE KEY unique_booking (user_id, event_id)
);

-- Insert sample events
INSERT INTO events (name, date, time, location, capacity) VALUES
('Yoga Session', '2025-04-07', '09:00:00', 'Studio A', 20),
('Meditation Workshop', '2025-04-08', '10:30:00', 'Studio B', 15),
('Dance Class', '2025-04-09', '14:00:00', 'Studio C', 12),
('Fitness Training', '2025-04-10', '16:30:00', 'Gym', 10),
('Art Workshop', '2025-04-11', '11:00:00', 'Art Room', 8);
