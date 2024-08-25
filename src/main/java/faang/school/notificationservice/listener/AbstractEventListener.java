package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    protected final List<NotificationService> notificationServices;
    protected final List<MessageBuilder<T>> messageBuilders;

    protected String getMessage(T event, Locale locale) {
        return messageBuilders.stream()
                .filter(m -> m.getInstance().equals(event.getClass()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No message builder found for " + event.getClass().getName()))
                .buildMessage(event, locale);
    }

    protected void sentNotification(long id, String message) {
        UserDto user = userServiceClient.getUser(id);
        user.setPreference(UserDto.PreferredContact.EMAIL);
        user.setEmail(user.getEmail());
        notificationServices.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Notification service not found, not preferred method for sending message"))
                .send(user, message);
    }
}