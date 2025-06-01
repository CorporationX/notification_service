package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public abstract class AbstractListener<T> {

    @Autowired
    protected ObjectMapper objectMapper; // глобальный, настроенный в JacksonConfig

    @Autowired
    protected UserServiceClient userServiceClient;

    @Autowired
    protected List<MessageBuilder<T>> messageBuilders;

    @Autowired
    protected List<NotificationService> notificationServices;

    private final Class<T> eventType;

    protected AbstractListener(Class<T> eventType) {
        this.eventType = eventType;
    }

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

    protected void handleEvent(Message message, Class<T> type, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), type);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}