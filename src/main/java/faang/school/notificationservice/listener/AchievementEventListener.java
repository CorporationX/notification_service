package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class AchievementEventListener extends AbstractEventListener<AchievementEvent> implements MessageListener {
    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<MessageBuilder<AchievementEvent>> messageBuilders,
                                    List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    @SneakyThrows
    public void onMessage(Message message, byte[] pattern) {
        var userLocale = Locale.GERMANY;
        try {
            var achievementEvent = objectMapper.readValue(message.getBody(), AchievementEvent.class);
            log.info("Successfully parsed AchievementEvent: {}", achievementEvent);

            var text = getMessage(achievementEvent, userLocale);
            log.debug("Generated notification message: {}", text);

            sendNotification(achievementEvent.getUserId(), text);
            log.info("Notification sent to userId: {}", achievementEvent.getUserId());
        } catch (IOException e) {
            log.error("Failed to parse AchievementEvent from message", e);
            throw e;
        }
    }
}
