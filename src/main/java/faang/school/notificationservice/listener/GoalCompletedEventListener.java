package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.dto.GoalDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder> messageBuilders;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEvent event = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            UserDto userReceiver = userServiceClient.getUser(event.getUserId());
            GoalDto goal = userServiceClient.getGoalById(event.getGoalId());
            event.setUserId(userReceiver.getId());
            event.setGoalId(goal.getId());

            String messageToSend = messageBuilders.stream()
                    .filter(builder -> builder.supportsEventType().equals(event.getClass()))
                    .findFirst()
                    .map(builder -> builder.buildMessage(event, Locale.getDefault()))
                    .orElseThrow(() ->
                            new RuntimeException("Builder not found " + event.getClass().getName()));

            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == userReceiver.getPreferredContact())
                    .findFirst()
                    .ifPresent(service -> service.send(userReceiver, messageToSend));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
