package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractEventListener<E> implements MessageListener {

    protected final ObjectMapper objectMapper;
    protected final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final List<MessageBuilder<E>> messageBuilders;

    protected String getMessage(UserDto user, E event) {
        log.info("Building message for user with id {}", user.getId());
        Optional<MessageBuilder<E>> messageBuilder = messageBuilders.stream()
                .filter(builder -> builder.getInstance().equals(event.getClass()))
                .findFirst();
        return messageBuilder
                .map(builder -> builder.buildMessage(user, event))
                .orElseThrow(() -> new IllegalArgumentException("No such event handler for " + event.getClass().getSimpleName()));
    }

    protected void sendNotification(UserDto user, String message) {
        log.info("Sending notification for user with id {}", user.getId());
        Optional<NotificationService> optionalService = notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact().equals(user.getPreferredContact()))
                .findFirst();
        optionalService.ifPresentOrElse(notificationService -> notificationService.send(user, message), () -> {
            String errorMessage = "Notification service not found for user %d".formatted(user.getId());
            log.info(errorMessage, user.getId());
            throw new IllegalArgumentException(errorMessage);
        });
    }

    @Override
    @Async("mainExecutorService")
    public abstract void onMessage(Message message, byte[] pattern);
}
