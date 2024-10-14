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
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<E> implements MessageListener {

    protected final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<E>> messageBuilders;

    protected String getMessage(E event, Locale locale) {
        Optional<MessageBuilder<E>> messageBuilder = messageBuilders.stream()
                .filter(builder -> builder.getInstance() == event.getClass())
                .findFirst();
        return messageBuilder
                .map(builder -> builder.buildMessage(event, locale))
                .orElseThrow(() -> new IllegalArgumentException("No such event handler for " + event.getClass().getSimpleName()));
    }

    protected void sendNotification(long userId, String message) {
        UserDto user = userServiceClient.getUser(userId);
        Optional<NotificationService> optionalService = notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreference()))
                .findFirst();
        optionalService.ifPresentOrElse(notificationService -> notificationService.send(user, message), () -> {
            String errorMessage = "Notification service not found for user %d".formatted(userId);
            log.info(errorMessage, userId);
            throw new IllegalArgumentException(errorMessage);
        });
    }
}
