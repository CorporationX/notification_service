package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.builder.FollowerMessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final FollowerMessageBuilder followerMessageBuilder;
    private final NotificationService notificationService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            FollowerEventDto followerEventDto = objectMapper.readValue(message.getBody(), FollowerEventDto.class);

            // Собрать сообщение
            // получить юзеров
            UserDto follower = userServiceClient.getUser(followerEventDto.getFollowerId());

            // отправить сообщение
            UserDto followee = userServiceClient.getUser(followerEventDto.getFolloweeId());
            // получить способ доставки
            //...
//            notificationService.send(followee, text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
