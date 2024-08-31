package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.CommentEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                List<MessageBuilder<CommentEvent>> messageBuilders) {

        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from redis: {}", new String(message.getBody(), StandardCharsets.UTF_8));
        CommentEvent commentEvent = constructEvent(message.getBody(), CommentEvent.class);
        sendMessage(commentEvent, commentEvent.getPostAuthorId(), Locale.US);
    }
}
