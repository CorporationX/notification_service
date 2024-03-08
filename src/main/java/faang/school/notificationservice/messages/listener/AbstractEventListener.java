package faang.school.notificationservice.messages.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messages.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final ObjectMapper objectMapper;

    protected String getMessage(T eventType, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType().equals(eventType.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.builderMessage(eventType, locale))
                .orElseThrow(() -> new IllegalArgumentException("Неподдерживаемый тип события: " + eventType.getClass()));
    }

    protected void sendNotification(long id, String message) {
        log.info("Подготавливаем запрос в FeignClient на получения пользователя с ID: {}", id);
        UserDto user = userServiceClient.getUser(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("У пользователя " + user.getUsername() +
                        ", нет предпочтений для уведомлений."))
                .send(user, message);
        log.info("Пользователю с ID: {} отправлено уведомление: {}", id, "Получен новый лайк поста");
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            log.info("Получено событие: {}", type.getName());
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Ошибка десериализации JSON в объект  " + type.getName(), e);
        }
    }
}
