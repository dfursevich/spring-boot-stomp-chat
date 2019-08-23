package by.fdf.chat.service;

import by.fdf.chat.model.ChatMessage;
import by.fdf.chat.protocol.ChatMessageNotification;
import by.fdf.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
@Service
public class MessageService {
    private ChatService chatService;
    private ChatMessageRepository chatMessageRepository;

    public MessageService(ChatService chatService, ChatMessageRepository chatMessageRepository) {
        this.chatService = chatService;
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getMessages(String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }

    public void sendMessage(String roomId, String userId, String sessionId, String message) {
        ChatMessage chatMessage = new ChatMessage(roomId, userId, sessionId, System.currentTimeMillis(), message);
        chatMessageRepository.save(chatMessage);

        chatService.broadcast(roomId, new ChatMessageNotification(chatMessage));
    }
}
