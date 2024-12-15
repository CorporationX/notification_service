package faang.school.notificationservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.GoalCompletedMessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;
    private final GoalCompletedMessageBuilder messageBuilder;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (message.getBody() == null) {
            log.error("Received no message to process");
            return;
        }

        try {
            GoalCompletedEventDto event = objectMapper.readValue(message.getBody(), GoalCompletedEventDto.class);
            log.info("Received goal completed event from Redis channel: {}", event);
            UserDto user = userServiceClient.getUser(event.getUserId());
            log.info("Received user from Redis channel: {}", user);
            String messageText = messageBuilder.buildMessage(event, Locale.getDefault());
            log.info("Received a message text from Redis channel: {}", messageText);
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == user.getPreferredContact())
                    .findFirst()
                    .ifPresentOrElse(service -> {
                        service.send(user, messageText);
                        log.info("Send a message to the user via, {}", service.getClass().getSimpleName());
                    }, () -> {
                        log.error("Message could not be sent to the user because service is out of work or not found");
                    });
        } catch (Exception e) {
            log.error("Failed to send a message", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
