package faang.school.notificationservice.messaging;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class AchievementListener extends EventListenerBase<AchievementDto> implements MessageListener {

    public AchievementListener(JsonMapper jsonMapper, UserServiceClient userServiceClient, List<MessageBuilder<AchievementDto>> messageBuilders, List<NotificationService> notificationServices) {
        super(jsonMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        jsonMapper.toObject(message.toString(), AchievementDto.class).ifPresent(
                achievement -> {
                    String m = getMessage(achievement, Locale.UK);
                    sendNotification(achievement.getUserId(),m);
                }
        );
    }
}
