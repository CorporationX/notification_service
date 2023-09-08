package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.achievement.AchievementDto;
import faang.school.notificationservice.dto.achievement.DtoUserEventAchievement;
import faang.school.notificationservice.dto.user.UserDto;
import faang.school.notificationservice.messageBuilder.AchievementMessageBuilder;
import faang.school.notificationservice.notification_sending_strategy.SendingNotification;
import faang.school.notificationservice.service.EmailService;
import faang.school.notificationservice.service.sms_sending.SmsTwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AchievementMessageSubscriber implements MessageListener {
    private final List<SendingNotification> sendingNotification;
    private final AchievementMessageBuilder achievementMessageBuilder;
    private final ObjectMapper objectMapper;
    private DtoUserEventAchievement dtoUserEventAchievement;
    private final SmsTwilioService smsTwilioService;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        try {
            dtoUserEventAchievement = objectMapper.readValue(message.getBody(), DtoUserEventAchievement.class);
        } catch (IOException ignored) {
        }
        AchievementDto achievementDto = dtoUserEventAchievement.getAchievement();
        String messageText = achievementMessageBuilder.buildMessage(dtoUserEventAchievement, "ru");
        UserDto userDto = achievementMessageBuilder.getUserDto();
        sendingNotification.sending(userDto.getEmail(), achievementDto.getTitle(), messageText);
        sendingNotification.stream().filter()
//        switch (userDto.getPreference()) {
//            case EMAIL -> sendingNotification.sending(userDto.getEmail(), achievementDto.getTitle()
//                    , messageText);
//            case PHONE -> smsTwilioService.sendSms(userDto, messageText);
//            case TELEGRAM -> System.out.println("telegram");
//        }
    }
}
