package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.like.LikeEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public LikeListener(ObjectMapper objectMapper,
                        UserServiceClient userServiceClient,
                        List<MessageBuilder<LikeEvent>> messageBuilders,
                        List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, event -> {
            sendNotification(event.getReceivedId(), getMessage(event, Locale.UK));
        });
    }
}