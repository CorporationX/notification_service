package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;
import java.util.Locale;

public class CommentEventListener extends AbstractListener<CommentEvent> implements MessageListener {

    public CommentEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<CommentEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, event -> {
            String text = getMessage(event, Locale.getDefault());
            sendNotification(event.getPostAuthorId(), text);
        });
    }
}
