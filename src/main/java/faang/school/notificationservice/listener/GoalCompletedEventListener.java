package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.goal.GoalCompletedEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEventDto> implements MessageListener {

    private final ObjectMapper objectMapper;

    public GoalCompletedEventListener(UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<GoalCompletedEventDto>> messageBuilders,
                                      ObjectMapper objectMapper) {
        super(userServiceClient, notificationServices, messageBuilders);
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            GoalCompletedEventDto event = objectMapper.readValue(message.getBody(), GoalCompletedEventDto.class);
            String localizedMessage = getMessage(event, Locale.getDefault());
            sendNotification(event.getUserId(), localizedMessage);
            log.info("Successfully processed goal completed event for user {} and goal {}",
                    event.getUserId(), event.getGoalId());
        } catch (Exception e) {
            log.error("Failed to handle GoalCompletedEvent message", e);
        }
    }
}

