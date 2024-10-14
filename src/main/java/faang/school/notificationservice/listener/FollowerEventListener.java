package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;


@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {
    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 MessageBuilder<FollowerEvent> messageBuilder,
                                 List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowerEvent event = handleEvent(message, FollowerEvent.class);
        String userMessage = getMessage(event, Locale.getDefault());
        sendNotification(event.followedId(), userMessage);
    }
}
