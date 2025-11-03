package hostel.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {
    private final int id;
    private final String recipientRollNumber;
    private final String message;
    private final String timestamp;
    private boolean read;

    public Notification(String recipientRollNumber, String message) {
        this(-1, recipientRollNumber, message, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), false);
    }

    public Notification(int id, String recipientRollNumber, String message, String timestamp, boolean read) {
        this.id = id;
        this.recipientRollNumber = recipientRollNumber;
        this.message = message;
        this.timestamp = timestamp;
        this.read = read;
    }

    public Notification(String recipientRollNumber, String message, String timestamp, boolean read) {
        this(-1, recipientRollNumber, message, timestamp, read);
    }

    public int getId() {
        return id;
    }

    public String getRecipientRollNumber() {
        return recipientRollNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }
}
