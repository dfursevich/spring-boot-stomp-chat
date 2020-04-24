package by.fdf.chat.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dzmitry Fursevich
 */
@Component
public class UserSessionStorage {
    private final Map<String, String> user2SessionId = new ConcurrentHashMap<>();

    public void put(String userId, String sessionId) {
        user2SessionId.put(userId, sessionId);
    }

    public String get(String userId) {
        return user2SessionId.get(userId);
    }

    public String remove(String userId) {
        return user2SessionId.remove(userId);
    }
}
