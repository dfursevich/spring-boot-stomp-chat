package by.fdf.chat.repository;

import by.fdf.chat.model.ChatSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public interface ChatSubscriptionRepository extends MongoRepository<ChatSubscription, String> {
    List<ChatSubscription> findByRoomIdAndEndTimeIsNull(String roomId);
    boolean existsByRoomIdAndUserIdAndEndTimeIsNull(String roomId, String userId);
}
