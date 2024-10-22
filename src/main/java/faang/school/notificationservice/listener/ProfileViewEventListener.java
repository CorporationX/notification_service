package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.ProfileViewEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final NotificationService notificationServiceImpl;
    private final UserServiceClient userServiceClient;
    private final MessageBuilder<ProfileViewEvent> messageBuilder;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("ProfileViewEventListener listens for messages");
            ProfileViewEvent event = objectMapper.readValue(message.getBody(), ProfileViewEvent.class);
            log.info("Aquired message with body: {}", message.getBody());
            UserDto profileAuthor = userServiceClient.getUser(event.getAuthorId());
            notificationServiceImpl.send(profileAuthor, messageBuilder.buildMessage(profileAuthor,event));
        } catch (IOException exception) {
            log.info("Failed to deserialize like event", exception);
        }
    }
}

