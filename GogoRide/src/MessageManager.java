import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MessageManager {
    private boolean isConnected;

    public MessageManager() {
        try {
            DatabaseManager.connect();
            isConnected = true;
        } catch (SQLException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public boolean sendMessage(String senderId, String receiverId, String content) {
        if (!isConnected) return false;

        try {
            Message message = new Message(
                java.util.UUID.randomUUID().toString(),
                senderId,
                receiverId,
                content,
                new Date(),
                false
            );
            DatabaseManager.saveMessage(message);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Message> getUserMessages(String userId) {
        if (!isConnected) return new ArrayList<>();

        try {
            return DatabaseManager.getUserMessages(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Message> getUnreadMessages(String userId) {
        if (!isConnected) return new ArrayList<>();

        try {
            return DatabaseManager.getUnreadMessages(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean markMessageAsRead(String messageId) {
        if (!isConnected) return false;

        try {
            DatabaseManager.markMessageAsRead(messageId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMessage(String messageId) {
        if (!isConnected) return false;

        try {
            DatabaseManager.deleteMessage(messageId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUnreadMessageCount(String userId) {
        if (!isConnected) return 0;

        try {
            return DatabaseManager.getUnreadMessageCount(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean markAllMessagesAsRead(String userId) {
        if (!isConnected) return false;

        try {
            DatabaseManager.markAllMessagesAsRead(userId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Message> getConversation(String userId1, String userId2) {
        if (!isConnected) return new ArrayList<>();

        try {
            return DatabaseManager.getConversation(userId1, userId2);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
