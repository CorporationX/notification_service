package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CommentEventListener extends AbstractListener<CommentEventDto> {

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEventDto>> messageBuilders,
                                List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topics.commentCreatedNotification}",
            groupId = "${spring.kafka.consumer.groupId}")
    public void handle(CommentEventDto event) {
        log.info("Comment event received: {}", event);
        sendNotification(event.getPostAuthorId(), getMessage(event, Locale.ENGLISH));
    }
}