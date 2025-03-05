package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.BusinessException;
import faang.school.notificationservice.exception.DataValidationException;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationStrategyService;
import faang.school.notificationservice.service.UserService;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {
    private static final String MESSAGE_BUILDER_NOT_FOUND =
            "Для типа события %s не найден MessageBuilder";
    private static final String NOT_FOUND_NOTIFICATION_SERVICE =
            "Не найден необходимый сервис отправки уведомления";

    protected final ObjectMapper objectMapper;
    protected final UserService userService;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void sendNotification(Long userId, String message) {
        UserDto userDto = userService.getUserById(userId);
        if (userDto.getPreference() == null) {
            throw new DataValidationException("У пользователя c ID " + userDto.getId()
                    + " отсутствует способ отправки уведомления");
        }
        notificationServices.stream()
                .filter(service -> service.getPreferredContact() == userDto.getPreference())
                .findFirst()
                .orElseThrow(() -> new BusinessException(NOT_FOUND_NOTIFICATION_SERVICE))
                .send(userDto, message);
    }

    protected T getEventFromBytes(byte[] body, Class<T> typeClass) {
        try {
            return objectMapper.readValue(body, typeClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder ->
                        messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format(MESSAGE_BUILDER_NOT_FOUND, event.getClass())
                ))
                .buildMessage(event, locale);
    }
}
