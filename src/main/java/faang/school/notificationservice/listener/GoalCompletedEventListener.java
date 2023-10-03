package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.GoalCompletedEvent;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Locale;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    public GoalCompletedEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             MessageBuilder<GoalCompletedEvent> messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        GoalCompletedEvent goalCompletedEvent = fromJsonToObject(message, GoalCompletedEvent.class);
        String messageToSend = getMessage(goalCompletedEvent, Locale.ENGLISH);
        sendNotification(goalCompletedEvent.getCompletedGoalId(), messageToSend);
    }
}
