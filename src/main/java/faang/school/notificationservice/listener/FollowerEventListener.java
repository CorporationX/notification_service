package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.FollowerMessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final FollowerMessageBuilder messageBuilder;
    private final UserServiceClient userServiceClient;
    private final List<NotificationService> notificationServices;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
        } catch (IOException e) {
            log.error("Error reading FollowerEvent from message body: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String text = messageBuilder.getText(event);
        UserDto user = userServiceClient.getUser(event.getFolloweeId());
        notificationServices.stream()
                .filter(notificationService -> notificationService.getPreferredContact() == user.getPreference())
                .findFirst()
                .ifPresent(notificationService -> notificationService.send(user, text));
        log.info("Sending notification to user: {}", user);
    }
}
