package by.fdf.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author Dzmitry Fursevich
 */
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatInboxItem {
    @Id
    private String id;
    private String userId;
    private String roomId;
    private String peerId;
    private String message;
    private Instant messageAt;
    private int unread;
}
