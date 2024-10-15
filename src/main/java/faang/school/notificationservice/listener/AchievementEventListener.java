package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.AchievementEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> implements MessageListener {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    MessageBuilder<AchievementEvent> messageBuilder,
                                    List<NotificationService> notifications) {
        super(objectMapper, userServiceClient, messageBuilder, notifications);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AchievementEvent event = handleEvent(message, AchievementEvent.class);
        String userMessage = getMessage(event, Locale.getDefault());
        sendNotification(event.userId(), userMessage);
    }
}
