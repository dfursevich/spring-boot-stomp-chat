package by.fdf.chat.repository;

import by.fdf.chat.model.ChatMessage2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public interface ChatMessage2Repository extends MongoRepository<ChatMessage2, String> {
    List<ChatMessage2> findByRoomId(String roomId);

    List<ChatMessage2> findByRoomIdAndReadAtIsNull(String roomId);
}
