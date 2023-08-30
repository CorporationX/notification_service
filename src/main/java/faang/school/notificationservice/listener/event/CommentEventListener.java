package faang.school.notificationservice.listener.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEventDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {
    public CommentEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEventDto>> messageBuilder,
                                List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto commentEventDto = readValue(message.getBody(), CommentEventDto.class);
        log.info("Sending notifications for comment: {}", commentEventDto);
        notify(commentEventDto);
    }

    private void notify(CommentEventDto event) {
        UserDto user = userServiceClient.getUser(event.getPostAuthorId());
        String message = getMessage(event, Locale.getDefault());
        sendNotification(user, message);
    }
}
