package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Evgenii Malkov
 */
@Component
@Slf4j
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> implements MessageListener {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<AchievementEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AchievementEvent achievementEvent = handleMessage(message, AchievementEvent.class);
        UserDto user = userServiceClient.getUser(achievementEvent.getUserId());
        String msg = getMessage(achievementEvent, user.getPreferredLocale());
        sendNotification(user, msg);
    }
}
