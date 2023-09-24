package faang.school.notificationservice.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.UserAchievementEvent;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener<UserAchievementEvent>
        implements MessageListener {
    @Autowired
    public AchievementEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                    MessageBuilder<UserAchievementEvent> messageBuilder,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        UserAchievementEvent userAchievementEvent = mapEvent(message, UserAchievementEvent.class);
        sendNotification(userAchievementEvent.getUserId(), getMessage(userAchievementEvent, Locale.getDefault()));
    }
}
