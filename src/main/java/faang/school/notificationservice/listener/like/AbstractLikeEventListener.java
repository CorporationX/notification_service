package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
public abstract class AbstractLikeEventListener extends AbstractEventListener<LikeEvent> {

    public AbstractLikeEventListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     List<NotificationService> notificationServices,
                                     Map<Class<?>, MessageBuilder<?>> messageBuilderMap) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilderMap);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
            String text = getMessage(likeEvent, Locale.getDefault());
            sendNotification(likeEvent.getAuthorPostId(), text);
        } catch (IOException e) {
            log.error("Failed to deserialize LikeEvent from Redis message: {}", new String(message.getBody()), e);
        }
    }
}
