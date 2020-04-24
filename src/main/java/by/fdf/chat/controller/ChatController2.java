package by.fdf.chat.controller;

import by.fdf.chat.model.ChatInboxItem;
import by.fdf.chat.model.ChatMessage2;
import by.fdf.chat.model.ChatRoom;
import by.fdf.chat.protocol.ChatInboxItemDto;
import by.fdf.chat.protocol.ChatMessage2Dto;
import by.fdf.chat.repository.ChatInboxItemRepository;
import by.fdf.chat.repository.ChatMessage2Repository;
import by.fdf.chat.repository.ChatRoomRepository;
import by.fdf.chat.service.UserSessionStorage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * SUBSCRIBE /user/queue/inbox
 * SUBSCRIBE /topic/messages/{roomId}
 * SEND /api/messages/user/{userId}
 * SEND /api/messages/room/{roomId}
 * SEND /api/messages/{messageId}/read
 * SEND /api/messages/room/{roomId}/read
 * SUBSCRIBE /api/inbox
 * SUBSCRIBE /api/messages/{roomId}
 *
 * @author Dzmitry Fursevich
 */
@Controller
public class ChatController2 {
    private static final String ROOM_MESSAGES_DESTINATION_FORMAT = "/topic/messages/%s";
    private static final String USER_INBOX_DESTINATION = "/queue/inbox";

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessage2Repository chatMessage2Repository;
    private final ChatInboxItemRepository chatInboxItemRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserSessionStorage userSessionStorage;

    public ChatController2(ChatRoomRepository chatRoomRepository,
                           ChatMessage2Repository chatMessage2Repository,
                           ChatInboxItemRepository chatInboxItemRepository,
                           SimpMessagingTemplate messagingTemplate,
                           UserSessionStorage userSessionStorage) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessage2Repository = chatMessage2Repository;
        this.chatInboxItemRepository = chatInboxItemRepository;
        this.messagingTemplate = messagingTemplate;
        this.userSessionStorage = userSessionStorage;
    }

    @MessageMapping("/messages/user/{userId}")
    public void sendMessageToUser(@DestinationVariable String userId, String msg, Principal principal) {
        var participantIds = new TreeSet<>(Set.of(userId, principal.getName()));
        var room = chatRoomRepository.findByParticipantIds(participantIds)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .participantIds(participantIds)
                        .createdAt(Instant.now())
                        .createdBy(principal.getName())
                        .build()));
        sendMessageToRoom(room, msg, principal);
    }

    @MessageMapping("/messages/room/{roomId}")
    public void sendMessageToRoom(@DestinationVariable String roomId, String msg, Principal principal) {
        var room = chatRoomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new);
        sendMessageToRoom(room, msg, principal);
    }

    private void sendMessageToRoom(ChatRoom room, String msg, Principal principal) {
        var message = chatMessage2Repository.save(ChatMessage2.builder()
                .roomId(room.getId())
                .message(msg)
                .sentBy(principal.getName())
                .sentAt(Instant.now())
                .build());
        messagingTemplate.convertAndSend(format(ROOM_MESSAGES_DESTINATION_FORMAT, room.getId()), new ChatMessage2Dto(message));

        room.getParticipantIds().stream()
                .map(pid -> chatInboxItemRepository.findByUserIdAndRoomId(pid, room.getId())
                        .orElse(ChatInboxItem.builder()
                                .userId(pid)
                                .roomId(room.getId())
                                .peerId(room.getParticipantIds().stream().filter(p -> !p.equals(pid)).findFirst().orElse(null))
                                .build()))
                .forEach(cii -> {
                    cii.setMessage(message.getMessage());
                    cii.setMessageAt(message.getSentAt());
                    cii.setUnread(cii.getUserId().equals(message.getSentBy()) ? cii.getUnread() : cii.getUnread() + 1);
                    cii = chatInboxItemRepository.save(cii);
                    replyToUser(cii.getUserId(), USER_INBOX_DESTINATION, cii);
                });
    }

    private boolean replyToUser(String userId, String destination, Object message) {
        var sessionId = userSessionStorage.get(userId);
        if (sessionId != null) {
            var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionId);
            headerAccessor.setLeaveMutable(true);
            messagingTemplate.convertAndSendToUser(sessionId, destination, message, headerAccessor.getMessageHeaders());
            return true;
        }
        return false;
    }

    @MessageMapping("/messages/{messageId}/read")
    public void sendMessageRead(@DestinationVariable String messageId, Principal principal) {
        var userId = principal.getName();
        var message = chatMessage2Repository.findById(messageId).orElseThrow(IllegalArgumentException::new);
        var room = chatRoomRepository.findById(message.getRoomId()).orElseThrow(IllegalArgumentException::new);
        //TODO::check permissions
        if (message.getSentBy().equals(userId) || message.getReadAt() != null) return;

        message.setReadBy(userId);
        message.setReadAt(Instant.now());
        chatMessage2Repository.save(message);
        messagingTemplate.convertAndSend(format(ROOM_MESSAGES_DESTINATION_FORMAT, message.getRoomId()), new ChatMessage2Dto(message));

        var inboxItem = chatInboxItemRepository.findByUserIdAndRoomId(userId, room.getId())
                .orElse(ChatInboxItem.builder()
                        .userId(userId)
                        .roomId(room.getId())
                        .peerId(room.getParticipantIds().stream().filter(p -> !p.equals(userId)).findFirst().orElse(null))
                        .build());
        inboxItem.setMessage(message.getMessage());
        inboxItem.setMessageAt(message.getSentAt());
        inboxItem.setUnread(inboxItem.getUnread() - 1);
        inboxItem = chatInboxItemRepository.save(inboxItem);
        replyToUser(inboxItem.getUserId(), USER_INBOX_DESTINATION, inboxItem);
    }

    @MessageMapping("/messages/room/{roomId}/read")
    public void sendMessagesRead(@DestinationVariable String roomId, Principal principal) {
        var now = Instant.now();
        var userId = principal.getName();
        var room = chatRoomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new);
        //TODO::check permissions
        chatMessage2Repository.findByRoomIdAndReadAtIsNull(roomId).stream()
                .filter(m -> !m.getSentBy().equals(userId))
                .forEach(m -> {
                    m.setReadBy(userId);
                    m.setReadAt(now);
                    m = chatMessage2Repository.save(m);
                    messagingTemplate.convertAndSend(format(ROOM_MESSAGES_DESTINATION_FORMAT, m.getRoomId()), new ChatMessage2Dto(m));
                });

        var inboxItem = chatInboxItemRepository.findByUserIdAndRoomId(userId, room.getId())
                .orElse(ChatInboxItem.builder()
                        .userId(userId)
                        .roomId(room.getId())
                        .peerId(room.getParticipantIds().stream().filter(p -> !p.equals(userId)).findFirst().orElse(null))
                        .build());
        inboxItem.setUnread(0);
        inboxItem = chatInboxItemRepository.save(inboxItem);
        replyToUser(inboxItem.getUserId(), USER_INBOX_DESTINATION, inboxItem);
    }

    //TODO: paging
    @SubscribeMapping("/inbox")
    public List<ChatInboxItemDto> subscribeRooms(Principal principal) {
        return chatInboxItemRepository.findByUserIdOrderByMessageAt(principal.getName()).stream()
                .map(ChatInboxItemDto::new)
                .collect(Collectors.toList());
    }

    //TODO: paging
    @SubscribeMapping("/messages/{roomId}")
    public List<ChatMessage2Dto> subscribeRoomMessages(@DestinationVariable String roomId, Principal principal) {
        var room = chatRoomRepository.findById(roomId).orElseThrow(IllegalArgumentException::new);
        //TODO::check permissions
        return chatMessage2Repository.findByRoomId(roomId)
                .stream()
                .map(ChatMessage2Dto::new)
                .collect(Collectors.toList());
    }
}
