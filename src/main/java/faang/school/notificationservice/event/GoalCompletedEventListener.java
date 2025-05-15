package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.exception.EventReadException;
import faang.school.notificationservice.exception.ExceptionMessage;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class GoalCompletedEventListener implements MessageListener {

    private final UserServiceClient userServiceClient;
    private final Map<Class<?>, MessageBuilder<?>> messageBuilders = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;

    @SuppressWarnings("unchecked")
    private MessageBuilder<GoalCompletedEvent> findBuilder(GoalCompletedEvent event) {
        MessageBuilder<?> builder = messageBuilders.get(event.getClass());
        if (builder == null) {
            throw new IllegalStateException("No MessageBuilder found for event type: " + event.getClass());
        }
        return (MessageBuilder<GoalCompletedEvent>) builder;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            Long userId = event.getUserId();
            log.info("Received GoalCompletedEvent: userId={}, goalId={}", userId, event.getGoalId());

            UserDto user = Optional.ofNullable(userServiceClient.getUser(userId))
                    .orElseThrow(() -> new EventReadException(ExceptionMessage.USER_NOT_FOUND, userId));

            String notificationMessage = "Goal completed: " + event.getGoalId();

            NotificationService service = notificationServices.stream()
                    .filter(s -> s.getPreferredContact() == user.getPreference())
                    .findFirst()
                    .orElseThrow(() -> new EventReadException(ExceptionMessage.PREFERENCE_NOT_FOUND, userId));

            service.send(user, notificationMessage);

        } catch (IOException e) {
            log.error("Error reading event from message {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
