package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.dto.NotificationData;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class CommentEventListener extends AbstractListener<CommentEvent> implements MessageListener {

    public CommentEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEvent>> messageBuilders,
                                List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEvent commentEvent = getEvent(message, CommentEvent.class);
        NotificationData notificationData = getNotificationData(commentEvent.getCommentAuthorId());
        String textOfMessage = getMessage(commentEvent, Locale.UK, notificationData);
        UserNotificationDto userDto = userServiceClient.getDtoForNotification(commentEvent.getPostAuthorId());
        sendNotification(userDto, textOfMessage);
        log.info("Sending notification for event {}", commentEvent);
    }
}
