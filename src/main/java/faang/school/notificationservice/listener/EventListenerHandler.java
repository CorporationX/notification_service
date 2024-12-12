package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EventListenerHandler<T> {
    private final List<MessageBuilder<T>> messageBuilders;
    private final List<NotificationService> notificationServices;
    private final ObjectMapper objectMapper;

    public void eventHandler(Message message, Class<T> eventType, Function<T, UserDto> handle) {
        try {
            T event = objectMapper.readValue(message.getBody(), eventType);
            UserDto user = handle.apply(event);

            String text = getMessage(event, user.getLocale());
            sendNotification(text, user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMessage(T event, Locale locale) {
        Optional<MessageBuilder<T>> message = messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance() == event.getClass())
                .findFirst();

        if (message.isPresent()) {
            return message.get().buildMessage(event, locale);
        }
        throw new IllegalStateException("can not find message builder");
    }

    private void sendNotification(String text, UserDto user) {
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .ifPresent(service -> service.send(user, text));
    }
}
