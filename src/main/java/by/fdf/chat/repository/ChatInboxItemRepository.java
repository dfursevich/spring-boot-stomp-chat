package by.fdf.chat.repository;

import by.fdf.chat.model.ChatInboxItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public interface ChatInboxItemRepository extends MongoRepository<ChatInboxItem, String> {
    Optional<ChatInboxItem> findByUserIdAndRoomId(String userId, String roomId);

    List<ChatInboxItem> findByUserIdOrderByMessageAt(String userId);
}
