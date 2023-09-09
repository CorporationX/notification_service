package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.achievement.DtoUserEventAchievement;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.AchievementMessageBuilder;
import faang.school.notificationservice.sender.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AchievementMessageSubscriber implements MessageListener {
    private final List<NotificationService> listNotification;
    private final AchievementMessageBuilder achievementMessageBuilder;
    private final ObjectMapper objectMapper;
    private DtoUserEventAchievement dtoUserEventAchievement;
    private final Map<UserDto.PreferredContact, NotificationService> sendNotifications = new HashMap<>(listNotification.size());

    @PostConstruct
    private void init() {
        for (NotificationService i : listNotification) {
            sendNotifications.put(i.getPreferredContact(), i);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            dtoUserEventAchievement = objectMapper.readValue(message.getBody(), DtoUserEventAchievement.class);
        } catch (IOException ignored) {
        }
        String messageText = achievementMessageBuilder.buildMessage(dtoUserEventAchievement, new Locale("ru"), "ru");
        UserDto userDto = achievementMessageBuilder.getUserDto();
        sendNotifications.get(userDto.getPreference()).send(userDto, messageText);
    }
}
