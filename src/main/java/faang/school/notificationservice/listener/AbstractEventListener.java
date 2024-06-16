package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;

    public T mapMessageBodyToEvent(byte[] messageBody, Class<T> eventType) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(messageBody, eventType);
        } catch (IOException e) {
            log.error("Deserializing failed: {0}", e);
            throw new RuntimeException(e);
        }
    }

    public void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);

        if (userDto.getPreference() == null) {
            log.info("User (id = {}) doesn't have preferred contact. Not sending notification.", userId);
            return;
        }

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .forEach(notificationService -> notificationService.send(userDto, message));
    }
}