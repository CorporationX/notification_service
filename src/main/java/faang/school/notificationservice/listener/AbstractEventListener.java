package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;

    protected void handleEvent(ConsumerRecord<String, String> record, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.convertValue(record.value(), type);
            consumer.accept(event);
        } catch (Exception e) {
            log.error("Error while processing event {}", record.value(), e);
            throw new RuntimeException("Error handling event");
        }
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst()
                .map(messageBuilder -> messageBuilder.buildMessage(event, locale))
                .orElseThrow(() -> new RuntimeException("Message builder not found"));
    }

    protected void sendNotification(Long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification service not found"))
                .send(user, message);
    }
}