package by.fdf.chat.service;

import by.fdf.chat.model.ChatSubscription;
import by.fdf.chat.protocol.ChatTypingNotification;
import by.fdf.chat.protocol.ChatUnsubscribeNotification;
import by.fdf.chat.repository.ChatSubscriptionRepository;
import by.fdf.chat.repository.ChatSubscriptionRepositoryCustom;
import by.fdf.chat.protocol.ChatSubscribeNotification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
@Service
public class RoomService {
    private ChatService chatService;
    private ChatSubscriptionRepository chatSubscriptionRepository;
    private ChatSubscriptionRepositoryCustom chatSubscriptionRepositoryCustom;

    public RoomService(ChatService chatService, ChatSubscriptionRepository chatSubscriptionRepository, ChatSubscriptionRepositoryCustom chatSubscriptionRepositoryCustom) {
        this.chatService = chatService;
        this.chatSubscriptionRepository = chatSubscriptionRepository;
        this.chatSubscriptionRepositoryCustom = chatSubscriptionRepositoryCustom;
    }

    public ChatSubscription subscribeRoom(String roomId, String userId, String sessionId) {
        ChatSubscription subscription = chatSubscriptionRepositoryCustom.getOrCreateNotFinishedSubscription(roomId, sessionId, userId);

        chatService.broadcast(roomId, new ChatSubscribeNotification(subscription));

        return subscription;
    }

    public void unsubscribeRoom(String roomId, String sessionId) {
        ChatSubscription finished = chatSubscriptionRepositoryCustom.finishAndGetSubscription(roomId, sessionId);

        if (finished != null) {
            chatService.broadcast(roomId, new ChatUnsubscribeNotification(finished));
        }
    }

    public void unsubscribeRooms(String sessionId) {
        List<ChatSubscription> finished = chatSubscriptionRepositoryCustom.finishAndGetSubscriptions(sessionId);
        finished.forEach(s -> chatService.broadcast(s.getRoomId(), new ChatUnsubscribeNotification(s)));
    }

    public List<ChatSubscription> getSubscriptions(String roomId) {
        return chatSubscriptionRepository.findByRoomIdAndEndTimeIsNull(roomId);
    }

    public boolean subscribed(String roomId, String userId) {
        return chatSubscriptionRepository.existsByRoomIdAndUserIdAndEndTimeIsNull(roomId, userId);
    }

    public void typing(String roomId, String sessionId, String userId) {
        chatService.broadcast(roomId, new ChatTypingNotification(roomId, sessionId, userId, System.currentTimeMillis()));
    }
}
