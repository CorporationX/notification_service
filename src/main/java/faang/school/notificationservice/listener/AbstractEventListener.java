package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserNotificationDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new EntityNotFoundException("Отсутствует message builder с типом: " +
                        event.getClass().getName()));
    }

    protected void sendNotification(Long userId, String messageText) {
        UserNotificationDto userDto = userServiceClient.getContactInfoById(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(userDto.getPreferredContact()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Отсутствует notification service с типом " +
                        userDto.getPreferredContact()))
                .send(userDto, messageText);
    }
}
