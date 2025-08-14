package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.EventStartEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class RedisMessageSubscriber implements MessageListener {
    NotificationService notificationService;
    UserServiceClient userServiceClient;
    MessageBuilder<EventStartEvent> messageBuilder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Object body = redisTemplate.getValueSerializer().deserialize(message.getBody());
        if (body instanceof EventStartEvent eventStartEvent) {
            for (Long participantsId : eventStartEvent.participantsIds()) {
                notificationService.send(userServiceClient.getUser(participantsId),
                        messageBuilder.buildMessage(eventStartEvent, Locale.ENGLISH));
            }
        }
    }
}
