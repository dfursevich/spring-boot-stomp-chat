package by.fdf.chat.protocol;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import by.fdf.chat.model.ChatMessage;

/**
 * @author Dzmitry Fursevich
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class ChatMessageNotification {
    private ChatMessageDto message;

    public ChatMessageNotification() {
    }

    public ChatMessageNotification(ChatMessage chatMessage) {
        this.message = new ChatMessageDto(chatMessage);
    }

    public ChatMessageDto getMessage() {
        return message;
    }

    public void setMessage(ChatMessageDto message) {
        this.message = message;
    }
}
