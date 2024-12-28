package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserContactsDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected void handleEvent(Message message, Class<T> event, Consumer<T> consumer) {
        try {
            T eventObject = objectMapper.readValue(message.getBody(), event);
            consumer.accept(eventObject);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to process event", e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Event type is not supported"))
                .buildMessage(event, userLocale);
    }

    protected void sendNotification(Long id, String message) {
        UserContactsDto user = userServiceClient.getUserContacts(id);
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User's preferred contact is not supported"))
                .send(user, message);
    }
}
