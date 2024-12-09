package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.messaging.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class ProjectFollowerEventListener extends AbstractEventListener implements MessageListener {

    public ProjectFollowerEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder> messageBuilders,
            List<NotificationService> notificationServices
    ) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received project follower message event: {}", message);
            ProjectFollowerEvent event = objectMapper.readValue(message.getBody(), ProjectFollowerEvent.class);
            log.info("PArced project follower event: {}", event);
            String text = getMessage(event, Locale.US);
            sendNotification(event.getFolloweeId(), text);
        } catch (Exception e) {
            log.error("Parsing error = {}, {}, for message = {}", e.getMessage(), e, message);
            throw new RuntimeException(e);
        }
    }
}
