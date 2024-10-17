package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component("commentEventListener")
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(List<NotificationService> notificationServices,
                                List<MessageBuilder<CommentEvent>> messageBuilders,
                                UserServiceClient userServiceClient,
                                ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent = handleEvent(message, CommentEvent.class);
        String answerMessage = getMessage(commentEvent, Locale.UK);
        sendNotification(commentEvent.getPostAuthorId(), answerMessage);
    }
}
