package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale userLocal) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType(event))
                .findFirst()
                .map(tMessageBuilder -> tMessageBuilder.buildMessage(event, userLocal))
                .orElseThrow(() -> new IllegalArgumentException("Illegal Argument Exception"));
    }

    protected void sendMessage(Long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Illegal Argument Exception"))
                .send(user, message);
    }
}
