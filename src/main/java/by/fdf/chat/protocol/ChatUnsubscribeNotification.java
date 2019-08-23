package by.fdf.chat.protocol;

import by.fdf.chat.model.ChatSubscription;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Dzmitry Fursevich
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class ChatUnsubscribeNotification {
    private ChatSubscriptionDto subscription;

    public ChatUnsubscribeNotification() {
    }

    public ChatUnsubscribeNotification(ChatSubscription subscription) {
        this.subscription = new ChatSubscriptionDto(subscription);
    }

    public ChatSubscriptionDto getSubscription() {
        return subscription;
    }

    public void setSubscription(ChatSubscriptionDto subscription) {
        this.subscription = subscription;
    }
}
