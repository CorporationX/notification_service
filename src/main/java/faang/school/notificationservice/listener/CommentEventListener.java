package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.CommentEvent;
import faang.school.notificationservice.exception.DeserializationException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationServiceResolver;
import faang.school.notificationservice.service.user.FeignUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class CommentEventListener extends NotificationListener<CommentEvent> {

    private final ObjectMapper objectMapper;

    public CommentEventListener(List<MessageBuilder<CommentEvent>> builders,
                                NotificationServiceResolver notificationServiceResolver,
                                FeignUserService feignUserService,
                                ObjectMapper objectMapper) {
        super(builders, notificationServiceResolver, feignUserService);
        this.objectMapper = objectMapper;
    }

    @Override
    protected Long recipientId(CommentEvent event) {
        return event.postAuthorId();
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.comment}",
            groupId = "${spring.kafka.group-id}"
    )
    public void listenCommentEvent(String message) {
        try {
            CommentEvent event = objectMapper.readValue(message, CommentEvent.class);
            handle(event);
        } catch (IOException e) {
            log.error("Failed to parse CommentEvent from Kafka message: {}", message, e);
            throw new DeserializationException(
                    "Failed to deserialize message to " + CommentEvent.class.getSimpleName(), e
            );
        }
    }
}
