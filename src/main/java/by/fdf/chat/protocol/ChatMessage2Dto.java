package by.fdf.chat.protocol;

import by.fdf.chat.model.ChatMessage2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author Dzmitry Fursevich
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage2Dto {
    private String id;
    private String roomId;
    private String message;
    private String sentBy;
    private Instant sentAt;
    private String readBy;
    private Instant readAt;

    public ChatMessage2Dto(ChatMessage2 message) {
        this.id = message.getId();
        this.roomId = message.getRoomId();
        this.message = message.getMessage();
        this.sentBy = message.getSentBy();
        this.sentAt = message.getSentAt();
        this.readBy = message.getReadBy();
        this.readAt = message.getReadAt();
    }
}
