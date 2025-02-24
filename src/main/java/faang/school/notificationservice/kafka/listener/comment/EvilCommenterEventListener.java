package faang.school.notificationservice.kafka.listener.comment;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.kafka.listener.EventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.comment.CommentEventService;
import faang.school.notificationservice.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvilCommenterEventListener implements EventListener {

    private final CommentEventService notificationService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<CommentEvent> messageBuilder;
    private final EventMapper<CommentEvent> eventMapper;

    @KafkaListener(topics = "${kafka.comment.topic}", groupId = "%{spring.kafka.group.id}")
    @Override
    public void listen(String message) {
        CommentEvent commentEvent = eventMapper.mapMessageToEvent(message, CommentEvent.class);
        log.info("Received comment event: {}", commentEvent);
        UserDto receiverUser = userServiceClient.getUser(commentEvent.postAuthorId());
        String messageToSend = messageBuilder.buildMessage(commentEvent, Locale.getDefault());
        notificationService.apply(receiverUser, messageToSend);
    }
}
