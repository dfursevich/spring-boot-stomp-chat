package by.fdf.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

/**
 * @author Dzmitry Fursevich
 */
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    private String id;
    private Set<String> participantIds;
    private Instant createdAt;
    private String createdBy;
}
