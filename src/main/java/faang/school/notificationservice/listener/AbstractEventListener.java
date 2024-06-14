package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.ListenerException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static faang.school.notificationservice.exception.message.ListenerExceptionMessage.MESSAGE_BUILDER_NOT_FOUND;

@Component
@Setter
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<T>> messageBuilders;

    public String getMessage(Class<?> eventType, Locale userLocale, T event) {
        Optional<MessageBuilder<T>> messageBuilder = messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(eventType))
                .findFirst();

        return messageBuilder
                .orElseThrow(() -> new ListenerException(MESSAGE_BUILDER_NOT_FOUND.getMessage()))
                .buildMessage(event, userLocale);
    }

    public void sendNotification(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);

        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreference()))
                .forEach(notificationService -> notificationService.send(userDto, message));
    }
}
