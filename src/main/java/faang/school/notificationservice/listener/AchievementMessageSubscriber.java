package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.achievement.UserEventAchievementDto;
import faang.school.notificationservice.listener.next.AbstractEventListener;
import faang.school.notificationservice.messageBuilder.AchievementMessageBuilder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class AchievementMessageSubscriber extends AbstractEventListener<UserEventAchievementDto, String> implements MessageListener {

    public AchievementMessageSubscriber(ObjectMapper objectMapper, Map notifications, Map messageBuilders, UserServiceClient userServiceClient) {
        super(objectMapper, notifications, messageBuilders, userServiceClient);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        UserEventAchievementDto dtoUserEventAchievement = getEvent(getMessageBody(message), UserEventAchievementDto.class);
        sendNotification(dtoUserEventAchievement.getUserId(), "You have received an achievement", getMessage(AchievementMessageBuilder.class, new Locale("ru")
                , dtoUserEventAchievement, ""));
    }
}
