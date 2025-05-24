import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/gogoride?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";  // Mot de passe par défaut vide pour XAMPP
    
    private static Connection connection;
    
    public static void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données réussie!");
            createTables();
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver introuvable. Assurez-vous que le pilote JDBC est dans le classpath.", e);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            System.err.println("URL: " + URL);
            System.err.println("Utilisateur: " + USER);
            throw e;
        }
    }
    
    private static void createTables() throws SQLException {
        // Drop tables if they exist (for testing purposes)
        String[] dropTables = {
            "DROP TABLE IF EXISTS messages",
            "DROP TABLE IF EXISTS bookings",
            "DROP TABLE IF EXISTS rides",
            "DROP TABLE IF EXISTS users"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String dropTable : dropTables) {
                stmt.execute(dropTable);
            }
            System.out.println("Tables existantes supprimées avec succès.");
        }

        String createUsersTable = """
            CREATE TABLE users (
                user_id VARCHAR(36) PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                email VARCHAR(100),
                phone_number VARCHAR(20),
                address TEXT,
                user_type VARCHAR(20) DEFAULT 'PASSENGER',
                rating DECIMAL(3,2) DEFAULT 5.0,
                total_rides INT DEFAULT 0,
                registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createRidesTable = """
            CREATE TABLE rides (
                ride_id VARCHAR(36) PRIMARY KEY,
                driver_id VARCHAR(36),
                origin VARCHAR(100) NOT NULL,
                destination VARCHAR(100) NOT NULL,
                ride_date DATE NOT NULL,
                departure_time TIME NOT NULL,
                total_seats INT NOT NULL,
                available_seats INT NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                status VARCHAR(20) DEFAULT 'SCHEDULED',
                description TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (driver_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;
        
        String createBookingsTable = """
            CREATE TABLE bookings (
                booking_id VARCHAR(36) PRIMARY KEY,
                ride_id VARCHAR(36),
                passenger_id VARCHAR(36),
                booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                status VARCHAR(20) DEFAULT 'PENDING',
                seats_booked INT DEFAULT 1,
                total_price DECIMAL(10,2),
                payment_status VARCHAR(20) DEFAULT 'PENDING',
                FOREIGN KEY (ride_id) REFERENCES rides(ride_id) ON DELETE CASCADE,
                FOREIGN KEY (passenger_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;
        
        String createMessagesTable = """
            CREATE TABLE messages (
                message_id VARCHAR(36) PRIMARY KEY,
                sender_id VARCHAR(36),
                receiver_id VARCHAR(36),
                content TEXT NOT NULL,
                sent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                is_read BOOLEAN DEFAULT FALSE,
                message_type VARCHAR(20) DEFAULT 'CHAT',
                related_ride_id VARCHAR(36),
                FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (related_ride_id) REFERENCES rides(ride_id) ON DELETE SET NULL
            )
        """;

        String createRatingsTable = """
            CREATE TABLE ratings (
                rating_id VARCHAR(36) PRIMARY KEY,
                ride_id VARCHAR(36),
                rater_id VARCHAR(36),
                rated_id VARCHAR(36),
                rating_value DECIMAL(3,2) NOT NULL,
                comment TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (ride_id) REFERENCES rides(ride_id) ON DELETE CASCADE,
                FOREIGN KEY (rater_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (rated_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            System.out.println("Création des tables...");
            
            stmt.execute(createUsersTable);
            System.out.println("Table 'users' créée avec succès");
            
            stmt.execute(createRidesTable);
            System.out.println("Table 'rides' créée avec succès");
            
            stmt.execute(createBookingsTable);
            System.out.println("Table 'bookings' créée avec succès");
            
            stmt.execute(createMessagesTable);
            System.out.println("Table 'messages' créée avec succès");
            
            stmt.execute(createRatingsTable);
            System.out.println("Table 'ratings' créée avec succès");
            
            System.out.println("Toutes les tables ont été créées avec succès!");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création des tables: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // User operations
    public static void saveUser(UserProfile user, String password) throws SQLException {
        String sql = "INSERT INTO users (user_id, username, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername()); // Fixed: use actual username
            pstmt.setString(3, password);
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            pstmt.executeUpdate();
        }
    }
    
    public static List<UserProfile> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        List<UserProfile> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new UserProfile(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                ));
            }
        }
        return users;
    }

    public static boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, use password hashing!
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if user exists with matching credentials
        }
    }

    public static UserProfile getUser(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserProfile(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
            }
        }
        return null;
    }

    public static UserProfile getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new UserProfile(
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
            }
        }
        return null;
    }

    public static boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public static void updateUser(UserProfile user) throws SQLException {
        String sql = "UPDATE users SET username = ?, first_name = ?, last_name = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName()); // Using firstName as username for now
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, ""); // Add lastName to UserProfile if needed
            pstmt.setString(4, user.getUserId());
            pstmt.executeUpdate();
        }
    }

    public static void deleteUser(String userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    // Ride operations
    public static void saveRide(Ride ride) throws SQLException {
        String sql = "INSERT INTO rides (ride_id, driver_id, origin, destination, ride_date, total_seats, available_seats, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, ride.getRideId());
            pstmt.setString(2, ride.getDriverName()); // You should update this to use driver_id
            pstmt.setString(3, ride.getOrigin());
            pstmt.setString(4, ride.getDestination());
            pstmt.setDate(5, new java.sql.Date(ride.getDate().getTime()));
            pstmt.setInt(6, ride.getTotalSeats());
            pstmt.setInt(7, ride.getAvailableSeats());
            pstmt.setString(8, ride.getStatus());
            pstmt.executeUpdate();
        }
    }
    
    public static List<Ride> searchRides(String origin, String destination) throws SQLException {
        String sql = "SELECT * FROM rides WHERE origin LIKE ? AND destination LIKE ? AND status = 'SCHEDULED'";
        List<Ride> rides = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + origin + "%");
            pstmt.setString(2, "%" + destination + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Ride ride = new Ride(
                    rs.getString("driver_id"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDate("ride_date").toString(),
                    rs.getInt("total_seats")
                );
                ride.setRideId(rs.getString("ride_id"));
                ride.setAvailableSeats(rs.getInt("available_seats"));
                ride.setStatus(rs.getString("status"));
                rides.add(ride);
            }
        }
        return rides;
    }

    public static void updateRideStatus(String rideId, String status) throws SQLException {
        String sql = "UPDATE rides SET status = ? WHERE ride_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, rideId);
            pstmt.executeUpdate();
        }
    }

    public static void updateRideAvailableSeats(String rideId, int availableSeats) throws SQLException {
        String sql = "UPDATE rides SET available_seats = ? WHERE ride_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, availableSeats);
            pstmt.setString(2, rideId);
            pstmt.executeUpdate();
        }
    }

    public static Ride getRide(String rideId) throws SQLException {
        String sql = "SELECT * FROM rides WHERE ride_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rideId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Ride ride = new Ride(
                    rs.getString("driver_id"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDate("ride_date").toString(),
                    rs.getInt("total_seats")
                );
                ride.setRideId(rs.getString("ride_id"));
                ride.setAvailableSeats(rs.getInt("available_seats"));
                ride.setStatus(rs.getString("status"));
                return ride;
            }
        }
        return null;
    }

    public static void deleteRide(String rideId) throws SQLException {
        String sql = "DELETE FROM rides WHERE ride_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rideId);
            pstmt.executeUpdate();
        }
    }
    
    // Message operations
    public static void saveMessage(Message message) throws SQLException {
        String sql = "INSERT INTO messages (message_id, sender_id, receiver_id, content, sent_date, is_read) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, message.getMessageId());
            pstmt.setString(2, message.getSenderId());
            pstmt.setString(3, message.getReceiverId());
            pstmt.setString(4, message.getContent());
            pstmt.setTimestamp(5, new java.sql.Timestamp(message.getSentDate().getTime()));
            pstmt.setBoolean(6, message.isRead());
            pstmt.executeUpdate();
        }
    }

    public static List<Message> getUserMessages(String userId) throws SQLException {
        String sql = "SELECT * FROM messages WHERE sender_id = ? OR receiver_id = ? ORDER BY sent_date DESC";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getString("message_id"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_date"),
                    rs.getBoolean("is_read")
                ));
            }
        }
        return messages;
    }

    public static List<Message> getUnreadMessages(String userId) throws SQLException {
        String sql = "SELECT * FROM messages WHERE receiver_id = ? AND is_read = false ORDER BY sent_date DESC";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getString("message_id"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_date"),
                    false
                ));
            }
        }
        return messages;
    }

    public static void markMessageAsRead(String messageId) throws SQLException {
        String sql = "UPDATE messages SET is_read = true WHERE message_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, messageId);
            pstmt.executeUpdate();
        }
    }

    public static void markAllMessagesAsRead(String userId) throws SQLException {
        String sql = "UPDATE messages SET is_read = true WHERE receiver_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }
    }

    public static List<Message> getConversation(String userId1, String userId2) throws SQLException {
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_date ASC";
        List<Message> messages = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId1);
            pstmt.setString(2, userId2);
            pstmt.setString(3, userId2);
            pstmt.setString(4, userId1);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getString("message_id"),
                    rs.getString("sender_id"),
                    rs.getString("receiver_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_date"),
                    rs.getBoolean("is_read")
                ));
            }
        }
        return messages;
    }

    public static void deleteMessage(String messageId) throws SQLException {
        String sql = "DELETE FROM messages WHERE message_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, messageId);
            pstmt.executeUpdate();
        }
    }

    public static int getUnreadMessageCount(String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = false";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
