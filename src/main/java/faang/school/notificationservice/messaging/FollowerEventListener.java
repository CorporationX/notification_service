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
    private final MessageBuilder<FollowerEvent> messageBuilder;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent followerEvent = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            String text = messageBuilder.buildMessage(followerEvent, Locale.ENGLISH);
            UserDto user = userServiceClient.getUser(followerEvent.followeeId());
            notificationServices.stream()
                    .filter(service -> service.getPreferredContact() == user.getPreference())
                    .findFirst()
                    .ifPresent(service -> service.send(user, text));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
