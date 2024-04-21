package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(MessageBuilder<CommentEvent> messageBuilder, List<NotificationService> notificationServices, UserServiceClient userServiceClient, ObjectMapper objectMapper) {
        super(messageBuilder, notificationServices, userServiceClient, objectMapper);
    }

    @KafkaListener(topics = "${spring.data.kafka.channels.comment-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(CommentEvent commentEvent) {
        sendNotification(commentEvent.getPostAuthorId(), getMessage(commentEvent, Locale.ENGLISH));
    }
}
