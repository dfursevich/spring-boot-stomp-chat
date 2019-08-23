package by.fdf.chat.protocol;

import by.fdf.chat.model.ChatSubscription;

/**
 * @author Dzmitry Fursevich
 */
public class ChatSubscriptionDto {
    private String userId;
    private String sessionId;
    private Long startTime;
    private Long endTime;

    public ChatSubscriptionDto() {
    }

    public ChatSubscriptionDto(String userId, String sessionId, Long startTime, Long endTime) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ChatSubscriptionDto(ChatSubscription subscription) {
        this.userId = subscription.getUserId();
        this.sessionId = subscription.getSessionId();
        this.startTime = subscription.getStartTime();
        this.endTime = subscription.getEndTime();
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
