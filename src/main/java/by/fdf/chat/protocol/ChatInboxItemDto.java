package by.fdf.chat.protocol;

import by.fdf.chat.model.ChatInboxItem;
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
public class ChatInboxItemDto {
    private String id;
    private String userId;
    private String roomId;
    private String peerId;
    private String message;
    private Instant messageAt;
    private int unread;

    public ChatInboxItemDto(ChatInboxItem item) {
        this.id = item.getId();
        this.userId = item.getUserId();
        this.roomId = item.getRoomId();
        this.peerId = item.getPeerId();
        this.message = item.getMessage();
        this.messageAt = item.getMessageAt();
        this.unread = item.getUnread();
    }
}
