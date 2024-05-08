package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractListener<CommentEvent> {
    protected CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                MessageBuilder<CommentEvent> messageBuilder,
                                List<NotificationService> notificationServicesList) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServicesList);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, commentEvent -> {
            String textMessage = getMessage(commentEvent, Locale.ENGLISH);
            sendNotification(commentEvent.getAuthorOfPostId(), textMessage);
        });
    }
}
