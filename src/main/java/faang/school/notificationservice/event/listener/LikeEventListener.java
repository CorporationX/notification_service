package faang.school.notificationservice.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    private final ObjectMapper objectMapper;
    private final NotificationServiceHandler notificationServiceHandler;
    private final MessageSource messageSource;

    public LikeEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            Map<Class<?>, MessageBuilder<?>> messageBuilders,
            NotificationServiceHandler notificationServiceHandler,
            MessageSource messageSource) {
        super(objectMapper, userServiceClient, messageBuilders);
        this.objectMapper = objectMapper;
        this.notificationServiceHandler = notificationServiceHandler;
        this.messageSource = messageSource;
    }

    @KafkaListener(topics = "like-events", groupId = "notification-group")
    public void handleLikeEvent(String eventJson) {
        try {
            LikeEvent event = objectMapper.readValue(eventJson, LikeEvent.class);
            log.info("Received LikeEvent: {}", event);

            UserDto user = getUser(event.getAuthorId());
            Locale userLocale = user.getLocale() != null ? Locale.forLanguageTag(user.getLocale()) : Locale.ENGLISH;

            String message = messageSource.getMessage("like.notification", new Object[]{event.getUserId(), event.getPostId()}, userLocale);

            notificationServiceHandler.sendNotification(user, message);
        } catch (Exception e) {
            log.error("Error processing LikeEvent: {}", eventJson, e);
        }
    }
}