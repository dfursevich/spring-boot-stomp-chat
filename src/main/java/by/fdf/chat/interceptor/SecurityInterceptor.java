package by.fdf.chat.interceptor;

import by.fdf.chat.service.UserSessionStorage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * @author Dzmitry Fursevich
 */
@Component
public class SecurityInterceptor implements ChannelInterceptor {
    private final UserSessionStorage userSessionStorage;

    public SecurityInterceptor(UserSessionStorage userSessionStorage) {
        this.userSessionStorage = userSessionStorage;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            var user = (User) ((UsernamePasswordAuthenticationToken) accessor.getUser()).getPrincipal();
            userSessionStorage.put(user.getUsername(), sessionId);
        }

        return message;
    }
}
