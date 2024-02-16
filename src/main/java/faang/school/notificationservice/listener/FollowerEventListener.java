package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.FollowerMessageBuilder;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {
    private FollowerEvent followerEvent;
    private ObjectMapper objectMapper;
    private UserServiceClient userServiceClient;
    private List<MessageBuilder> messageBuilders;
    private FollowerMessageBuilder followerMessageBuilder;
    private EmailService emailService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // перевести стрингу в FollowerEvent
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // получить юзеров
        UserDto follower = userServiceClient.getUser(followerEvent.getFollowerId());
        UserDto followee = userServiceClient.getUser(followerEvent.getFolloweeId());
        // получить текст сообщения
        String messageToSend = followerMessageBuilder.buildMessage(follower.getUsername());
        // получить способ доставки

        // отправить сообщение
        emailService.sendMessage("lions222@mail.ru", "Follow", messageToSend);
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
