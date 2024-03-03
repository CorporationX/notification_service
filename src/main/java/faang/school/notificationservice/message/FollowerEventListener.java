package faang.school.notificationservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;
    private TelegramService telegramService;
    @Value("${follower}")
    private String messageText;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserDto user = userServiceClient.getUser(event.getFolloweeId());
        String text = event.getFolloweeId() + messageText;
        telegramService.send(user, messageText);
    }
}
