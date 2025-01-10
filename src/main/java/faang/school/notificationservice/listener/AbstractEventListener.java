package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationService;
    private final MessageBuilder<T> messageBuilders;

    public T mapMessage(Message message, Class<T> tClass) {
        log.info("convert message to string");
        String convertedMessage = new String(message.getBody());
        try {
            log.info("mapping message to event class");
            return objectMapper.readValue(convertedMessage, tClass);
        } catch (JsonProcessingException e) {
            log.error("invalid json body for class " + tClass.getSimpleName(), e);
            throw new IllegalArgumentException("Неверный Json класс", e);
        }
    }

    public String getMessage(T event, long userId) {
        log.info("getting userDto from userService");
        UserDto userDto = userServiceClient.getUser(userId);
        log.info("calling message builder to build message");
        return messageBuilders.buildMessage(event, userDto.getLocale());
    }

    public void sendMessage(long userId, String message) {
        log.info("getting userDto from userService");
        UserDto userDto = userServiceClient.getUser(userId);

        log.info("getting notification and send message");
        notificationService.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Такого сервиса для отправки сообщения не существует"))
                .send(userDto, message);
    }
}
