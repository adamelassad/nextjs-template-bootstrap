import java.util.Date;
import java.sql.Timestamp;

public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private Date sentDate;
    private boolean isRead;

    public Message(String messageId, String senderId, String receiverId, String content, Date sentDate, boolean isRead) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentDate = sentDate;
        this.isRead = isRead;
    }

    // Constructor that accepts Timestamp for database operations
    public Message(String messageId, String senderId, String receiverId, String content, Timestamp sentDate, boolean isRead) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentDate = new Date(sentDate.getTime());
        this.isRead = isRead;
    }

    // Getters
    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public java.sql.Timestamp getTimestamp() {
        return new java.sql.Timestamp(sentDate.getTime());
    }

    public boolean isRead() {
        return isRead;
    }

    // Setters
    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "From: " + senderId + "\nTo: " + receiverId + "\nContent: " + content;
    }
}
