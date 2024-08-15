package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void getMessage(T event, Locale locale) {
        messageBuilders.stream().filter(messageBuilder ->
                        messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No such event " + event.getClass().getName()))
                .buildMessage(event, locale);
    }

    protected void sendNotification(long id, String message) {
        UserDto userDto = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService ->
                        notificationService.getPreferredContact().equals(userDto.getPreference()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No such notificationService for user " + userDto.getPreference()))
                .send(userDto, message);
    }
}
