package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
public class FollowEventListener implements MessageListener {
    private final UserServiceClient userServiceClient;
    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            FollowEventDto event = objectMapper.readValue(json, FollowEventDto.class);

            UserDto followee = userServiceClient.getUser(event.followeeId());
            // UserDto.PreferredContact contact = followee.getPreference();

            UserDto.PreferredContact contact = notificationService.getPreferredContact();
            notificationService.send(followee,"Some message");

            System.out.println("✅ Получено событие подписки:");
            System.out.println("Follower: " + event.followerId() + ", Followee: " + event.followeeId());

            // здесь можно отправить push/email уведомление, например:
            // notificationService.sendFollowNotification(event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
