package faang.school.notificationservice.listener.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.achievement.AchievementEvent;
import faang.school.notificationservice.dto.recommendation.RecommendationReceivedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
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
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<AchievementEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, AchievementEvent.class, event -> {
            String text = getMessage(event, Locale.getDefault());
            sendNotification(event.userId(), text);
        });
    }
}
