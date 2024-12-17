package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.messaging.ProjectFollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class ProjectFollowerEventListener extends AbstractEventHandler {

    @Value("${spring.data.redis.channel.follower-event-channel}")
    private String folowerTopic;

    public ProjectFollowerEventListener(
            RedisMessageListenerContainer container,
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder> messageBuilders,
            List<NotificationService> notificationServices
    ) {
        super(container, objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received project follower message event: {}", message);
        handleEvent(message, ProjectFollowerEvent.class, (t) -> {
            ProjectFollowerEvent event = (ProjectFollowerEvent) t;
            String text = getMessage(event, Locale.US);
            sendNotification(event.getFolloweeId(), text);
        });

    }

    @Override
    protected String getTopicName() {
        return folowerTopic;
    }
}
