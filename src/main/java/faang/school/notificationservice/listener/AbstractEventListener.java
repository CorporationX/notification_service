package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;

    public T mapMessageBodyToEvent(Object messageBody, Class<T> eventType) {
        return objectMapper.convertValue(messageBody, eventType);
    }

    public void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .forEach(notificationService -> notificationService.send(userDto, message));
    }
}
