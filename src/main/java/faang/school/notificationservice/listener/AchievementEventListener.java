package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserAchievementEventDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener<UserAchievementEventDto> {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    MessageBuilder<UserAchievementEventDto> messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        UserAchievementEventDto achievementEventDto = fromJsonToObject(message, UserAchievementEventDto.class);
        String messageToSend = getMessage(achievementEventDto, Locale.ENGLISH);
        sendNotification(achievementEventDto.getUserId(), messageToSend);
    }
}
