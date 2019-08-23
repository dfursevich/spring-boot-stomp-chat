package by.fdf.chat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dzmitry Fursevich
 */
@Document
public class ChatSubscription {
    @Id
    private String id;
    @Indexed
    private String roomId;
    private String userId;
    @Indexed
    private String sessionId;
    private Long startTime;
    private Long endTime;

    public ChatSubscription() {
    }

    public ChatSubscription(String roomId, String userId, String sessionId, Long startTime, Long endTime) {
        this.roomId = roomId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
