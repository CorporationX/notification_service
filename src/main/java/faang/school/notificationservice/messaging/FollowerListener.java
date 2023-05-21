package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.service.message.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class FollowerListener extends AbstractEventListener implements MessageListener {

    public FollowerListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                            List<NotificationService> notificationServices, List<MessageBuilder<?>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            var follower = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            log.info("Received message: {}", follower);
            String msg = getMessage(follower.getClass(), Locale.US);
            sendNotification(follower.getFolloweeId(), msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
