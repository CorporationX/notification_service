package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationService;
    private final List<MessageBuilder<T>> messageBuilders;

    public T mapMessage(Message message, Class<T> tClass) {
        String convertedMessage = new String(message.getBody());
        try {
            return objectMapper.readValue(convertedMessage, tClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Неверный Json класс", e);
        }
    }

    public String getMessage(T event, long userId) {
        UserDto userDto = userServiceClient.getUser(userId);
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Такого билдера сообщений не существует"))
                .buildMessage(event, userDto.getLocale());
    }

    public void sendMessage(long userId, String message) {
        UserDto userDto = userServiceClient.getUser(userId);
        notificationService.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Такого сервиса для отправки сообщения не существует"))
                .send(userDto, message);
    }
}
