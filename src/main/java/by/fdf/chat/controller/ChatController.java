package by.fdf.chat.controller;

import by.fdf.chat.model.ChatSubscription;
import by.fdf.chat.protocol.ChatMessageDto;
import by.fdf.chat.protocol.ChatSubscribeNotification;
import by.fdf.chat.protocol.ChatSubscriptionDto;
import by.fdf.chat.service.MessageService;
import by.fdf.chat.service.RoomService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dzmitry Fursevich
 */
@Controller
public class ChatController {

    private RoomService roomService;
    private MessageService messageService;

    public ChatController(RoomService roomService, MessageService messageService) {
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @SubscribeMapping("/room.{roomId}")
    public ChatSubscribeNotification subscribeRoom(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, Principal principal) {
        ChatSubscription subscription = roomService.subscribeRoom(roomId, principal.getName(), sessionId);
        return new ChatSubscribeNotification(subscription);
    }

    @SubscribeMapping("/room.{roomId}/subscriptions")
    public List<ChatSubscriptionDto> getRoomSubscriptions(@DestinationVariable String roomId) {
        return roomService.getSubscriptions(roomId).stream()
                .map(ChatSubscriptionDto::new)
                .collect(Collectors.toList());
    }

    @SubscribeMapping("/room.{roomId}/messages")
    public List<ChatMessageDto> getRoomMessages(@DestinationVariable String roomId) {
        return messageService.getMessages(roomId).stream()
                .map(ChatMessageDto::new)
                .collect(Collectors.toList());
    }

    @MessageMapping("/room.{roomId}/message")
    public void sendMessage(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, Principal principal, String message) {
        messageService.sendMessage(roomId, principal.getName(), sessionId, message);
    }

    @MessageMapping("/room.{roomId}/typing")
    public void sendTyping(@DestinationVariable String roomId, @Header("simpSessionId") String sessionId, Principal principal) {
        roomService.typing(roomId, sessionId, principal.getName());
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        roomService.unsubscribeRooms(event.getSessionId());
    }
}
