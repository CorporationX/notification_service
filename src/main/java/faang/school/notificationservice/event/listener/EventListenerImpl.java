package faang.school.notificationservice.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.UserRegisteredEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
public class EventListenerImpl extends AbstractEventListener<UserRegisteredEvent> {
    private final ObjectMapper objectMapper;
    private final NotificationServiceHandler notificationServiceHandler;
    private final MessageSource messageSource;

    public EventListenerImpl(
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

    public void processEvent(String eventJson) {
        try {
            UserRegisteredEvent event = objectMapper.readValue(eventJson, UserRegisteredEvent.class);
            log.info("Processing event: {}", event);

            UserDto user = getUser(event.getUserId());
            Locale userLocale = user.getLocale() != null ? Locale.forLanguageTag(user.getLocale()) : Locale.ENGLISH;

            String message = messageSource.getMessage("follower.new", null, userLocale);

            notificationServiceHandler.sendNotification(user, message);
        } catch (Exception e) {
            log.error("Failed to process event: {}", eventJson, e);
        }
    }
}