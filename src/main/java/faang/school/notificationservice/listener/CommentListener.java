package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.exception.NonRetryableException;
import faang.school.notificationservice.exception.RetryableException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class CommentListener extends AbstractEventListener<CommentEvent> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;

    public CommentListener(List<MessageBuilder<CommentEvent>> messageBuilders,
                           List<NotificationService> notificationServices,
                           ObjectMapper objectMapper,
                           UserServiceClient userServiceClient) {
        super(messageBuilders, notificationServices);
        this.objectMapper = objectMapper;
        this.userServiceClient = userServiceClient;
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.commentNew}",
            groupId = "comment",
            containerFactory = "commentListenerContainerFactory")
    public void consume(String data) {
        try {
            CommentEvent commentEvent = objectMapper.readValue(data, CommentEvent.class);
            log.info("New event id={} received from Kafka: {}", commentEvent.id(), data);
            String message = getMessage(commentEvent, Locale.getDefault());
            log.debug("For event id={} built message={}", commentEvent.id(), message);
            UserDto receiver = userServiceClient.getUser(commentEvent.postAuthorId());
            log.debug("Got receiver id={} from user-service", commentEvent.id());

            sendNotification(receiver, message);

            log.info("Notification sent to user id={} with text='{}'", receiver.getId(), message);
        } catch (RetryableException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NonRetryableException(e);
        }
    }
}
