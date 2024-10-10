package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/*
Когда Арстан свой таск закроет, возьму его реализацию
 */

@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;
    private final UserServiceClient userService;
    private final List<NotificationService> services;
    private final MessageBuilder messageBuilder;

    public T handleEvent(Message event, Class<T> type) {
        try {
            return objectMapper.readValue(event.getBody(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage(T event, Locale locale) {
        return messageBuilder.buildMessage(event, locale);
    }

    public void sendNotification(long userId, String message) {
        UserDto user = userService.getUser(userId);
        services.stream()
                .filter(service -> service.getPreferredContact().equals(user.getPreference()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No notification service found"
                        + " for the user`s preferred communication method"))
                .send(user, message);
    }
}