package by.fdf.chat.config;

import by.fdf.chat.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author Dzmitry Fursevich
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/queue", "/topic");
//        registry.setApplicationDestinationPrefixes("/api", "/topic");
//        registry.setUserDestinationPrefix("/user");
//    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/api", "/topic/");
        registry.enableStompBrokerRelay("/queue/", "/topic/")
                .setSystemHeartbeatSendInterval(60000)
                .setSystemHeartbeatReceiveInterval(60000)
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new SecurityInterceptor());
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
