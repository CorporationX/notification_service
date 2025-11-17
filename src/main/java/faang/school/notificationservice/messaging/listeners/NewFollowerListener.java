package faang.school.notificationservice.messaging.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.error.UserNotFoundException;
import faang.school.notificationservice.dto.events.NewFollowerEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.messaging.core.AbstractEventListener;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class NewFollowerListener extends AbstractEventListener<NewFollowerEventDto> {

    @Value("${app.locale.default:en}")
    private String defaultLocale;

    public NewFollowerListener(ObjectMapper mapper,
                               UserServiceClient userServiceClient,
                               List<NotificationService> notificationServices,
                               List<MessageBuilder<?>> messageBuilders) {
        super(mapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    protected Class<NewFollowerEventDto> getEventType() {
        return NewFollowerEventDto.class;
    }

    @KafkaListener(
            topics = "${app.topics.follower-create-events}",
            groupId = "${spring.kafka.consumer.group-id:notification-service}"
    )
    public void onMessage(String json) {
        NewFollowerEventDto event = null;
        try {
            event = readEvent(json);

            final UserDto receiver;
            try {
                receiver = loadUser(event.receiverId());
            } catch (UserNotFoundException e) {
                log.warn("Receiver with id {} not found, skip notification", event.receiverId());
                return;
            }

            Locale locale = resolveLocale(receiver.locale(), defaultLocale);

            String message = getMessage(event, locale);
            sendNotification(receiver, message);

            log.debug("Processed NewFollowerEvent followerId={} targetUserId={}",
                    event.actorId(), event.receiverId());

        } catch (Exception e) {
            log.error("Failed to process NewFollowerEvent: event={} json={}", event, json, e);
            throw e; // to Kafka error handler (retry/DLT)
        }
    }
}