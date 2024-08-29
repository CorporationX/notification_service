package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final MessageBuilder<T> messageBuilder;
    protected final UserService userService;

    protected T handleEvent(Class<T> clazz, Message message) {
        try {
            return objectMapper.readValue(message.getBody(), clazz);
        } catch (IOException e) {
            log.error("Failed to deserialize event", e);
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }

    protected void handleNotification(UserDto notifiedUser,
                                      UserDto eventInitiator,
                                      Object[] additionData) {
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact()
                        .equals(notifiedUser.getPreference()))
                .findFirst()
                .ifPresent(notificationService ->
                        notificationService.send(notifiedUser, messageBuilder
                                .buildMessage(eventInitiator, Locale.getDefault(), additionData)));
    }
}