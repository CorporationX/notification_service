package faang.school.notificationservice.listener.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final Utils utils;

    protected void sendNotification(UserDto userDto, String message) {
        notificationServices.stream()
            .filter(service -> service.getPreferredContact().equals(userDto.getPreference()))
            .findFirst()
            .ifPresent(service -> service.send(userDto, message));
    }

    protected T getEventDto(String message, Class<T> eventClass) {
        log.debug("get event for notification.\n{}", message);
        try {
            return objectMapper.readValue(message, eventClass);
        } catch (JsonProcessingException e) {
            logError(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
            .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
            .findFirst()
            .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
            .orElseThrow(() -> new NoSuchElementException(
                utils.format("There is no class implementing a message for {}",
                    event.getClass().getName()))
            );
    }

    abstract void logError(Exception e);
}
