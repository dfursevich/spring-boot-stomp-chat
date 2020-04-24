package by.fdf.chat.repository;

import by.fdf.chat.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findByParticipantIds(Set<String> participantIds);
}
