package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.AchievementEvent;
import faang.school.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AchievementListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final UserServiceClient userServiceClient;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            var achievement = objectMapper.readValue(message.getBody(), AchievementEvent.class);
            var user = userServiceClient.getUser(achievement.getUserId());
            emailService.sendEmail(user.getEmail(), "Achievement unlocked!",
                    "You have unlocked the achievement: " + achievement.getTitle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
