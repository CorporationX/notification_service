package faang.school.notificationservice.kafka.producer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Locale;

@Setter
@Getter
@RequiredArgsConstructor
public abstract class AbstractEventKafkaListener<T> {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    public String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportsEventType().equals(event.getClass()))
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Нет варианта текста уведомления"
                                + event.getClass().getName()
                                + "для {} на языке региона {}."
                                + userLocale));
    }

    protected void sendNotification(Long receiverId, String message) {
        UserDto user = userServiceClient.getUser(receiverId);
        notificationServices.stream()
                .filter(notificationService
                        -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(()
                        -> new IllegalArgumentException(
                                "Невозможна отправка уведомлений посредством "
                                        + user.getPreference()))
                .send(user, message);
    }
}
