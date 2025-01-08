package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.GoalCompletedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> implements MessageListener {

    public GoalCompletedEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationService,
            MessageBuilder<GoalCompletedEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEvent event = mapMessage(message, GoalCompletedEvent.class);
        String text = getMessage(event, event.getGoalId());
        sendMessage(event.getUserId(), text);
    }
}
