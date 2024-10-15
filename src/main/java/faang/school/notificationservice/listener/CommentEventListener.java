package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.CommentEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {
    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                MessageBuilder<CommentEvent> messageBuilder,
                                List<NotificationService> list) {
        super(objectMapper, userServiceClient, messageBuilder, list);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent = handleEvent(message, CommentEvent.class);
        String userMessage = getMessage(commentEvent, Locale.getDefault());
        sendNotification(commentEvent.postAuthorId(), userMessage);
    }
}
