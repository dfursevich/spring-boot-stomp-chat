package by.fdf.chat.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Dzmitry Fursevich
 */
@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcast(String roomId, Object message) {
        broadcastInner("/topic/room." + roomId, message);
    }

    private void broadcastInner(String topic, Object message) {
        messagingTemplate.convertAndSend(topic, message);
    }
}
