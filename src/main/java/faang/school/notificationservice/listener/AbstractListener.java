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
public abstract class AbstractListener<T> {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<MessageBuilder<T>> messageBuilders;
    protected final List<NotificationService> notificationServices;

    protected String getMessage(T event, Locale locale){
        return messageBuilders.stream()
                .filter(messageBuilder -> messageBuilder.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No message found for the given event type " + event.getClass().getName()))
                .buildMessage(event, locale);
    }

    protected void sendNotification(Long id, String message){
        UserDto user = userServiceClient.getUser(id);
        UserDto.PreferredContact userPref = user.getPreference() != null
                ? user.getPreference()
                : UserDto.PreferredContact.TELEGRAM;

        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(userPref))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Ошибка: не нашлось метода отправки который выбрал user"))
                .send(user, message);
    }

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer){
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}