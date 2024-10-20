package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class FollowEventListener extends AbstractEventListener<FollowEvent> implements MessageListener {

    public FollowEventListener(ObjectMapper objectMapper,
                               UserServiceClient userServiceClient,
                               List<MessageBuilder<FollowEvent>> messageBuilders,
                               List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowEvent event = objectMapper.readValue(message.getBody(), FollowEvent.class);
            String text = buildMessage(event, Locale.UK);
            sendNotification(event.getFolloweeId(), text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
