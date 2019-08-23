package by.fdf.chat.repository;

import by.fdf.chat.model.ChatSubscription;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dzmitry Fursevich
 */
@Repository
public class ChatSubscriptionRepositoryCustomImpl implements ChatSubscriptionRepositoryCustom {

    private MongoTemplate mongoTemplate;

    public ChatSubscriptionRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ChatSubscription getOrCreateNotFinishedSubscription(String roomId, String sessionId, String userId) {
        Query query = new Query(Criteria
                .where("roomId").is(roomId)
                .and("sessionId").is(sessionId)
                .and("endTime").is(null));

        Update update = new Update()
                .setOnInsert("roomId", roomId)
                .setOnInsert("sessionId", sessionId)
                .setOnInsert("userId", userId)
                .setOnInsert("startTime", System.currentTimeMillis());

        FindAndModifyOptions options = new FindAndModifyOptions()
                .upsert(true)
                .returnNew(true);

        return mongoTemplate.findAndModify(query, update, options, ChatSubscription.class);
    }

    @Override
    public ChatSubscription finishAndGetSubscription(String roomId, String sessionId) {
        Query query = new Query(Criteria
                .where("roomId").is(roomId)
                .and("sessionId").is(sessionId)
                .and("endTime").is(null));

        Update update = new Update()
                .set("endTime", System.currentTimeMillis());

        FindAndModifyOptions options = new FindAndModifyOptions()
                .returnNew(true);

        return mongoTemplate.findAndModify(query, update, options, ChatSubscription.class);
    }

    @Override
    public List<ChatSubscription> finishAndGetSubscriptions(String sessionId) {
        Query query = new Query(Criteria
                .where("sessionId").is(sessionId)
                .and("endTime").is(null));

        List<ChatSubscription> subscriptions = mongoTemplate.find(query, ChatSubscription.class);
        return subscriptions.stream()
                .peek(s -> s.setEndTime(System.currentTimeMillis()))
                .map(s -> mongoTemplate.save(s))
                .collect(Collectors.toList());
    }
}
