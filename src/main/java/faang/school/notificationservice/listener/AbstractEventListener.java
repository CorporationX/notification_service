package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationService;
    protected final MessageBuilder<T> messageBuilders;

    protected void handleEvent(Message message, Class<T> type) {
        T event = getEvent(message, type);
        String messageText = getMessage(event, Locale.UK);
        sendNotification(getIdRequestAuthor(event), messageText);
    }

    private T getEvent(Message message, Class<T> type) {
        try {
            return objectMapper.readValue(message.getBody(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long getIdRequestAuthor(T event) {
        return messageBuilders.getRequestAuthor(event);
    }

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.buildMessage(event, locale);
    }

    protected void sendNotification(long id, String messageText) {
        UserDto user = userServiceClient.getUser(id);
        notificationService.stream().filter(service -> service.getPreferredContact() == (user.getContactPreference()))
                .findFirst()
                .ifPresent(service -> service.send(user, messageText));
    }
}