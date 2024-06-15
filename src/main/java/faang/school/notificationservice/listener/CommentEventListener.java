package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component("commentEventListener")
public class CommentEventListener extends AbstractEventListener<CommentEventDto> implements MessageListener {
    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                MessageBuilder<CommentEventDto> commentEventMessageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, commentEventMessageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto event = mapMessageBodyToEvent(message.getBody(), CommentEventDto.class);

        log.info("Received new event {}", event);

        String notificationText = getMessageBuilder().buildMessage(event, Locale.getDefault());

        log.info("Built message {}", notificationText);

        sendNotification(event.getPostAuthorId(), notificationText);
    }
}