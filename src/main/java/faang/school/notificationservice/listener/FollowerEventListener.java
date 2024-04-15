package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent event = objectMapper.readValue(message.getBody(), FollowerEvent.class);
            sendNotification(event.getFollowerId(), getMessage(event, Locale.ENGLISH));
        } catch (IOException e) {
            log.error("IOException while deserializing FollowerEvent", e);
            throw new SerializationException("Could not deserialize FollowerEvent", e);
        }
    }
}
