package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.service.NotificationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Setter
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {

    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from channel {}: {}", new String(message.getChannel()), new String(message.getBody()));
        handleEvent(message, FollowerEvent.class,
                followerEvent -> sendNotification(followerEvent.getFollower(), getMessage(followerEvent)));
    }
}
