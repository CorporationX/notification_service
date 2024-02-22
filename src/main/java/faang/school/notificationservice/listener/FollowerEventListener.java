package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.builder.FollowerMessageBuilder;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.service.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final UserServiceClient userServiceClient;
    private final FollowerMessageBuilder followerMessageBuilder;
    private final EmailService emailService;
    private List<MessageBuilder> messageBuilders;
    private List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // перевести стрингу в FollowerEvent
        try {
            objectMapper.registerModule(new JavaTimeModule());
            FollowerEventDto followerEventDto = objectMapper.readValue(message.getBody(), FollowerEventDto.class);
            // получить юзеров
            UserDto follower = userServiceClient.getUser(followerEventDto.getFollowerId());
            UserDto followee = userServiceClient.getUser(followerEventDto.getFolloweeId());
            // получить текст сообщения
            String text = followerMessageBuilder.buildMessage(follower.getUsername());
            // получить способ доставки

            // отправить сообщение
            emailService.sendMessage("lions222@mail.ru", "Follow", text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
