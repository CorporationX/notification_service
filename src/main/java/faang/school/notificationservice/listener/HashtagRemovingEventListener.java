package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.HashtagRemovingEvent;
import faang.school.notificationservice.exception.EventReadException;
import faang.school.notificationservice.exception.ExceptionMessage;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.repository.NotificationEventLogRepository;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class HashtagRemovingEventListener extends AbstractEventListener<HashtagRemovingEvent> {

    public HashtagRemovingEventListener(ObjectMapper objectMapper,
                                        UserServiceClient userServiceClient,
                                        List<NotificationService> notificationServices,
                                        List<MessageBuilder<HashtagRemovingEvent>> messageBuilders,
                                        NotificationEventLogRepository repository) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, repository);
    }

    @KafkaListener(
            topics = "${spring.data.kafka.topic.hashtag-notification.name}",
            groupId = "${spring.data.kafka.consumer.group-id}"
    )
    public void receive(String message) {
        try {
            log.debug("Received new hashtag event: {}", message);
            HashtagRemovingEvent event = objectMapper.readValue(message, HashtagRemovingEvent.class);
            sendNotification(event.userId(), getMessage(event, Locale.UK));
        } catch (IOException e) {
            throw new EventReadException(ExceptionMessage.EVENT_READ_EXCEPTION, e);
        }
    }
}
