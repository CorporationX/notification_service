package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<ProfileViewEvent> messageBuilder;

    @EventListener
    public void handleMessage(String jsonEvent) {
        ProfileViewEvent event = readEvent(jsonEvent);
        log.info("Received message from channel: {}", jsonEvent);
        UserDto profileAuthor = userServiceClient.getUser(event.getAuthorId());
        notificationService.send(profileAuthor, messageBuilder.buildMessage(event, profileAuthor.getLocale()));
    }

    private ProfileViewEvent readEvent(String jsonEvent) {
        try {
            log.info("reading message {}", jsonEvent);
            return objectMapper.readValue(jsonEvent, ProfileViewEvent.class);
        } catch (JsonProcessingException exception) {
            log.error("message was not downloaded {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
