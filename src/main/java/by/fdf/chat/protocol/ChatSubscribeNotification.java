package by.fdf.chat.protocol;

import by.fdf.chat.model.ChatSubscription;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Dzmitry Fursevich
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class ChatSubscribeNotification {
    private ChatSubscriptionDto subscription;

    public ChatSubscribeNotification() {
    }

    public ChatSubscribeNotification(ChatSubscriptionDto subscription) {
        this.subscription = subscription;
    }

    public ChatSubscribeNotification(ChatSubscription subscription) {
        this.subscription = new ChatSubscriptionDto(subscription);
    }

    public ChatSubscriptionDto getSubscription() {
        return subscription;
    }

    public void setSubscription(ChatSubscriptionDto subscription) {
        this.subscription = subscription;
    }
}
