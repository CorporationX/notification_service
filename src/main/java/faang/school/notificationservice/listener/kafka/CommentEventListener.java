package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.kafkaevents.CommentEvent;
import faang.school.notificationservice.exception.EventReadException;
import faang.school.notificationservice.exception.ExceptionMessage;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.NotificationType;
import faang.school.notificationservice.repository.NotificationEventLogRepository;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent>  {

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                List<MessageBuilder<CommentEvent>> messageBuilders,
                                NotificationEventLogRepository repository
    ) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, repository);
    }

    @KafkaListener(topics = "${spring.data.kafka.topic.comment}", groupId = "${spring.data.kafka.group-id}")
    void onMessage(String message, Acknowledgment ack)  {
        try {
            CommentEvent event = objectMapper.readValue(message, CommentEvent.class);

            if (!checkNotificationExisting(event.id(), NotificationType.COMMENT, ack)) {
                sendNotification(event.postAuthorId(), getMessage(event, Locale.UK));
            }
        } catch (JsonProcessingException e) {
            throw new EventReadException(ExceptionMessage.EVENT_READ_EXCEPTION, e);
        }
    }

}
