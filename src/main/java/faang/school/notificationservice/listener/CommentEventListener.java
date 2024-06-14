package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component("commentEventListener")
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {
    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                MessageBuilder<CommentEvent> commentEventMessageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, commentEventMessageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent event = mapMessageBodyToEvent(message.getBody(), CommentEvent.class);
        String notificationText = getMessageBuilder().buildMessage(event, Locale.getDefault());
        sendNotification(event.getPostAuthorId(), notificationText);
    }
}