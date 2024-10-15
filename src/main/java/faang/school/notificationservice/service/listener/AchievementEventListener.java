package faang.school.notificationservice.service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.AchievementEvent;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> implements MessageListener {

    public AchievementEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                    List<MessageBuilder<AchievementEvent>> messageBuilders,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, AchievementEvent.class, achievementEvent -> {
            UserDto user = userServiceClient.getUser(achievementEvent.getUserId());
            String text = getMessage(achievementEvent, Locale.UK);
            sendNotification(user, text);
        });
    }
}
