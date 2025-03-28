package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class EventsListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notifyServices;

    protected void handleEvent(Message message, Class<T> eventType, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.supportEventType() == event.getClass())
                .findFirst()
                .map(tMessageBuilder -> tMessageBuilder.buildMessage(event, userLocale))
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }

    protected void sendNotification(UserDto receiver, String textMessage) {
        notifyServices.stream()
                .filter(service -> service.getPreferredContact() == receiver.getPreference())
                .findFirst()
                .ifPresent(service -> service.send(receiver, textMessage));
    }
}
