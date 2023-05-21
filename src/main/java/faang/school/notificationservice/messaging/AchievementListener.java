package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.service.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class AchievementListener extends AbstractEventListener implements MessageListener {

    public AchievementListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                               List<NotificationService> notificationServices, List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            var achievement = objectMapper.readValue(message.getBody(), AchievementEvent.class);
            String msg = getMessage(achievement.getClass(), Locale.US);
            sendNotification(achievement.getUserId(), msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
