package faang.school.notificationservice.messaging.likepost;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class LikePostEventListener extends AbstractEventListener<LikePostEvent> implements MessageListener {

    public LikePostEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<LikePostEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikePostEvent.class, event -> {
            String text = buildText(event, Locale.ENGLISH);
            Long postAuthorId = event.getPostAuthorId();
            sendNotification(postAuthorId, text);
            log.info("Notification was sent, postAuthorId: {}, text: {}", postAuthorId, text);
        });
    }
}
