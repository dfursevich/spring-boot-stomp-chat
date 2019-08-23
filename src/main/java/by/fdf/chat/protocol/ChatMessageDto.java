package by.fdf.chat.protocol;

import by.fdf.chat.model.ChatMessage;

/**
 * @author Dzmitry Fursevich
 */
public class ChatMessageDto {
    private String roomId;
    private String userId;
    private String sessionId;
    private Long timestamp;
    private String message;

    public ChatMessageDto() {
    }

    public ChatMessageDto(ChatMessage chatMessage) {
        this.roomId = chatMessage.getRoomId();
        this.userId = chatMessage.getUserId();
        this.sessionId = chatMessage.getSessionId();
        this.timestamp = chatMessage.getTimestamp();
        this.message = chatMessage.getMessage();
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
