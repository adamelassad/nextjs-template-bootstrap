import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private boolean isConnected;

    public UserManager() {
        try {
            DatabaseManager.connect();
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public boolean addUser(UserProfile user) {
        if (!isConnected) return false;
        
        try {
            if (isUsernameRegistered(user.getFirstName())) {
                return false; // User already exists
            }
            DatabaseManager.saveUser(user);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserProfile> getAllUsers() {
        if (!isConnected) return new ArrayList<>();
        
        try {
            return DatabaseManager.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public UserProfile getUser(String userId) {
        if (!isConnected) return null;
        
        try {
            return DatabaseManager.getUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUsernameRegistered(String username) {
        if (!isConnected) return false;
        
        try {
            return DatabaseManager.isUsernameExists(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(UserProfile user) {
        if (!isConnected) return false;
        
        try {
            DatabaseManager.updateUser(user);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(String userId) {
        if (!isConnected) return false;
        
        try {
            DatabaseManager.deleteUser(userId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
