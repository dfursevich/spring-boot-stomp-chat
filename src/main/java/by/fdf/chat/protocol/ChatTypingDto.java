package by.fdf.chat.protocol;

/**
 * @author Dzmitry Fursevich
 */
public class ChatTypingDto {
    private String roomId;
    private String sessionId;
    private String userId;
    private Long time;

    public ChatTypingDto() {
    }

    public ChatTypingDto(String roomId, String sessionId, String userId, Long time) {
        this.roomId = roomId;
        this.sessionId = sessionId;
        this.userId = userId;
        this.time = time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
