package faang.school.notificationservice.listener.achievement;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.achievement.AchievementEventDto;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class AchievementListener extends AbstractEventListener<AchievementEventDto> {

    public AchievementListener(ObjectMapper objectMapper,
                               UserServiceClient userServiceClient,
                               List<MessageBuilder<AchievementEventDto>> messageBuilder,
                               List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AchievementEventDto achievementDto = readValue(message.getBody(), AchievementEventDto.class);
        String notification = getMessage(achievementDto, Locale.getDefault());
        UserDto user = userServiceClient.getUser(achievementDto.getAuthorId());
        sendNotification(user, notification);
        log.info("Sending notifications for achievement: {}", achievementDto);
    }
}
