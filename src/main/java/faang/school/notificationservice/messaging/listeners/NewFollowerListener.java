package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.core.AbstractEventListener;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.events.NewFollowerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class NewFollowerListener extends AbstractEventListener<NewFollowerEvent> {

    @Value("${app.locale.new-follower:en}")
    private String localeTag;

    public NewFollowerListener(ObjectMapper mapper,
                               UserServiceClient userServiceClient,
                               List<NotificationService> notificationServices,
                               List<MessageBuilder<? extends NewFollowerEvent>> messageBuilders) {
        super(mapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<NewFollowerEvent> getEventType() {
        return NewFollowerEvent.class;
    }

    /**
     * Consumes JSON events from Kafka, builds a localized message, and sends a notification
     * using the user's preferred channel.
     * Topic is configured in application.yaml under app.topics.follower
     * Group id defaults to spring.kafka.consumer.group-id (or "notification-service").
     */
    @KafkaListener(
            topics = "${app.topics.follower}",
            groupId = "${spring.kafka.consumer.group-id:notification-service}"
    )
    public void onMessage(String json) {
        try {
            NewFollowerEvent event = readEvent(json);

            Locale locale = Locale.forLanguageTag(localeTag);

            String message = getMessage(event, locale);
            sendNotification(event.getTargetUserId(), message);

            log.debug("Processed NewFollowerEvent followerId={} targetUserId={}",
                    event.getFollowerId(), event.getFollowerId());
        } catch (Exception e) {
            log.error("Failed to process NewFollowerEvent: {}", json, e);
            throw e;
        }
    }
}