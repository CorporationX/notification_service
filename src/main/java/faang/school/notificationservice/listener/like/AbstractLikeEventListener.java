package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
public abstract class AbstractLikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public AbstractLikeEventListener(ObjectMapper objectMapper,
                                     UserServiceClient userServiceClient,
                                     List<NotificationService> notificationServices,
                                     List<MessageBuilder<LikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
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
