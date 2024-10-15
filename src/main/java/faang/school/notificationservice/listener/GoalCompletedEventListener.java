package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.GoalCompletedEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> implements MessageListener {
    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      MessageBuilder<GoalCompletedEvent> messageBuilder,
                                      List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        GoalCompletedEvent goalCompletedEvent = handleEvent(message, GoalCompletedEvent.class);
        String userMessage = getMessage(goalCompletedEvent, Locale.getDefault());
        sendNotification(goalCompletedEvent.userId(), userMessage);
    }
}
