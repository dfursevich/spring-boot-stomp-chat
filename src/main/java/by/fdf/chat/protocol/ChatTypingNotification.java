package by.fdf.chat.protocol;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Dzmitry Fursevich
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class ChatTypingNotification {

    private ChatTypingDto typing;

    public ChatTypingNotification() {
    }

    public ChatTypingNotification(String roomId, String sessionId, String userId, Long time) {
        this.typing = new ChatTypingDto(roomId, sessionId, userId, time);
    }

    public ChatTypingDto getTyping() {
        return typing;
    }

    public void setTyping(ChatTypingDto typing) {
        this.typing = typing;
    }
}
