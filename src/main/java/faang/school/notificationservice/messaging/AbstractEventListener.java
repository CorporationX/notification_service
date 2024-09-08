package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    public void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage(T event, Locale userLocale) {
        return messageBuilders.stream()
                .filter(mb -> mb.supportsEventType() == (event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such messageBuilder for "
                        + event.getClass().getName() + " found."))
                .buildMessage(event, userLocale);
    }

    protected void sendNotification(Long receiverId, String messageToSend) {
        UserDto postAuthor = userServiceClient.getUser(receiverId);
        notificationServices.stream()
                .filter(notificationService -> postAuthor.getPreference()
                        == (notificationService.getPreferredContact()))
                .findFirst()
                .ifPresent(notificationService
                        -> notificationService.send(postAuthor, messageToSend));
    }

}
