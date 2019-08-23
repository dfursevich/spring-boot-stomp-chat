package by.fdf.chat.repository;

import by.fdf.chat.model.ChatSubscription;

import java.util.List;

/**
 * @author Dzmitry Fursevich
 */
public interface ChatSubscriptionRepositoryCustom {
    ChatSubscription getOrCreateNotFinishedSubscription(String roomId, String sessionId, String userId);

    ChatSubscription finishAndGetSubscription(String roomId, String sessionId);

    List<ChatSubscription> finishAndGetSubscriptions(String sessionId);
}
