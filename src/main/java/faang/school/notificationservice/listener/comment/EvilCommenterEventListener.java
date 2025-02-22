package faang.school.notificationservice.listener.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.listener.EventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.comment.CommentEventNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvilCommenterEventListener implements EventListener {

    private final CommentEventNotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<CommentEvent> messageBuilder;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.comment.topic}", groupId = "%{spring.kafka.group.id}")
    @Override
    public void listen(String input) {
        CommentEvent commentEvent = mapInputToCommentEvent(input);
        log.info("Received comment event: {}", commentEvent);
        UserDto receiverUser = userServiceClient.getUser(commentEvent.postAuthorId());
        String message = messageBuilder.buildMessage(commentEvent, Locale.getDefault());
        notificationService.send(receiverUser, message);
    }

    private CommentEvent mapInputToCommentEvent(String input) {
        try {
            return objectMapper.readValue(input, CommentEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
