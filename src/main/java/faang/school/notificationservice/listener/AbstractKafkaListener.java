package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.client.user_service.UserClientResponseDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class AbstractKafkaListener<T, R> {
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<R>> messageBuilders;
    private final ObjectMapper objectMapper;
    private final Class<T> eventClass;
    private final Class<R> messageModelClass;

    public T getEvent(String message) {
        try {
            return objectMapper.readValue(message, eventClass);
        } catch (JsonProcessingException ex) {
            // TODO: другое исключение
            throw new RuntimeException();
        }
    }

    public String getMessage(R event, Locale locale) {
        return messageBuilders.stream()
                .filter(builder -> builder.getInstance() == messageModelClass)
                .findAny()
                // TODO: другой тип исключения
                .orElseThrow(RuntimeException::new)
                // TODO: локаль из юзера
                .buildMessage(event, locale);
    }

    public void sendNotification(UserClientResponseDto user, String text) {
        notificationServices.stream()
                .filter(service -> Objects.equals(service.getPreferredContact(), user.getPreference()))
                .findAny()
                .ifPresent(service -> service.send(user, text));
    }
}
