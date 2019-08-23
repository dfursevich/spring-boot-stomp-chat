package by.fdf.chat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dzmitry Fursevich
 */
@Document
public class ChatMessage {
    @Id
    private String id;
    @Indexed
    private String roomId;
    private String userId;
    private String sessionId;
    private Long timestamp;
    private String message;

    public ChatMessage() {
    }

    public ChatMessage(String roomId, String userId, String sessionId, Long timestamp, String message) {
        this.roomId = roomId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
