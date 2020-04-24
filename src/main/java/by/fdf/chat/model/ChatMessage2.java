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
public class ChatMessage2 {
    @Id
    private String id;
    private String roomId;
    private String message;
    private String sentBy;
    private Instant sentAt;
    private Instant readAt;
    private String readBy;
}
