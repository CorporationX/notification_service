package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final List<NotificationService> notificationServices;
    private final UserServiceClient userServiceClient;
    private final List<MessageBuilder> messageBuilders;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            UserDto follower = userServiceClient.getUser(event.getFollowerId());
            UserDto followee = userServiceClient.getUser(event.getFolloweeId());
            String text = messageBuilders.stream()
                            .filter(builder -> builder.getInstance() == FollowerEvent.class)
                            .findFirst()
                    .orElseThrow()
                    .buildMessage(event, Locale.ENGLISH);
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == followee.getPreference())
                    .findFirst()
                    .ifPresentOrElse(
                            service -> service.send(followee, text),
                            () -> {}
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
