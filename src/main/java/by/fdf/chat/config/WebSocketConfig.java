package by.fdf.chat.config;

import by.fdf.chat.interceptor.SecurityInterceptor;
import by.fdf.chat.service.UserSessionStorage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * @author Dzmitry Fursevich
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    private final SecurityInterceptor securityInterceptor;
    private final UserSessionStorage userSessionStorage;

    public WebSocketConfig(SecurityInterceptor securityInterceptor, UserSessionStorage userSessionStorage) {
        this.securityInterceptor = securityInterceptor;
        this.userSessionStorage = userSessionStorage;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/api");
        registry.setUserDestinationPrefix("/user");
    }

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setUserDestinationPrefix("/user");
//        registry.setApplicationDestinationPrefixes("/api", "/topic/");
//        registry.enableStompBrokerRelay("/queue/", "/topic/")
//                .setSystemHeartbeatSendInterval(60000)
//                .setSystemHeartbeatReceiveInterval(60000)
//                .setRelayHost("localhost")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest");
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(securityInterceptor);
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user instanceof User) {
            userSessionStorage.remove(((User) user).getUsername());
        }
    }
}
