package faang.school.notificationservice.publis.listener.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.comment.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.publis.listener.AbstractEventListener;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> implements MessageListener {
    public CommentEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder<CommentEventDto>> messageBuilders,
            List<NotificationService> notificationServices
    ) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto commentEvent;
        String messageBody = new String(message.getBody());
        log.info("Received messageBody: " + messageBody);

        try {
            commentEvent = objectMapper.readValue(messageBody, CommentEventDto.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        String notificationMsg = getMessage(commentEvent, Locale.getDefault());
        log.info("Successful message receipt.");

        sendNotification(commentEvent.getPostAuthorId(), notificationMsg);
        log.info("Successful notification sending.");
    }
}
