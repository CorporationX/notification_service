package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventStartEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;
    private final MessageSource messageSource;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Starting to process event start listener");
            EventStartEvent eventStartEvent = objectMapper.readValue(message.getBody(), EventStartEvent.class);
            List<UserDto> users = userServiceClient.getUsersByEventId(eventStartEvent.getEventId());
            log.info("Fetched {} users for event ID: {}", users.size(), eventStartEvent.getEventId());
            notifyUsers(users, eventStartEvent);
        } catch (IOException e) {
            log.error("Failed to process event start message", e);
            throw new RuntimeException("Failed to process event start message", e);
        }
    }

    private void notifyUsers(List<UserDto> users, EventStartEvent eventStartEvent) {
        String notificationMessage = eventStartEvent.getEventDelayTime().getMessage(eventStartEvent.getEventName(), messageSource, Locale.ENGLISH);
        log.info("Notifying {} users about event: {}", users.size(), eventStartEvent.getEventName());

        for (UserDto user : users) {
            notificationServices.stream()
                    .filter(notificationService -> user.getPreference() == notificationService.getPreferredContact())
                    .forEach(notificationService -> notificationService.send(user, notificationMessage));
        }
    }
}
