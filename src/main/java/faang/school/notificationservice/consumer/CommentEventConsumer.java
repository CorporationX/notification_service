package faang.school.notificationservice.consumer;

import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.messaging.CommentMessageBuilder;

import faang.school.notificationservice.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventConsumer {

    private final NotificationServiceImpl notificationService;
    private final CommentMessageBuilder messageBuilder;

    @KafkaListener(topics = "${kafka.topic.notifications}",
            groupId = "notification-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleCommentEvent(CommentEventDto event) {
        log.info("Received CommentEvent: {}", event);

        String message = messageBuilder.buildMessage(event, Locale.getDefault());
        log.info("Sending notification to userId {}: {}", event.postAuthorId(), message);

        notificationService.send(event.postAuthorId(), message);
    }
}
