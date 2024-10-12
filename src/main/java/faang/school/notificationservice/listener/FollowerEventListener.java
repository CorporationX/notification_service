package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Follower listener received a message");
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            UserDto user = userServiceClient.tryGetUser(event.getFolloweeId());
            String text = getMessage(user, event);
            sendNotification(user, text);
            log.info("Follower listener sent a message to user {}", event.getFolloweeId());
        } catch (IOException exception) {
            log.info("Failed to deserialize follower event", exception);
        }
    }
}
