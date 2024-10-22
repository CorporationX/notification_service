package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.event.LikeEvent;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener implements MessageListener {

    public AchievementEventListener(
            ObjectMapper objectMapper, UserServiceClient userServiceClient,
            List<MessageBuilder<LikeEvent>> messageBuilders, List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            AchievementEvent event = objectMapper.readValue(message.getBody(), AchievementEvent.class);
            sendNotification(event.getUserId(), buildMessage(event, Locale.UK));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTopic() {
        return RedisTopics.ACHIEVEMENT_CHANNEL.getTopic();
    }

}
