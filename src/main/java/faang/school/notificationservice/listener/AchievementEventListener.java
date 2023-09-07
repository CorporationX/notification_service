package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEventDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class AchievementEventListener extends AbstractEventListener<AchievementEventDto> {

    public AchievementEventListener(ObjectMapper objectMapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    List<MessageBuilder<Class<?>>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AchievementEventDto achievementEventDto = convertToJSON(message, AchievementEventDto.class);
        String message2 = getMessage(achievementEventDto.getClass(), Locale.ENGLISH);
        sendNotification(achievementEventDto.getReceiverId(), message2);
    }
}
