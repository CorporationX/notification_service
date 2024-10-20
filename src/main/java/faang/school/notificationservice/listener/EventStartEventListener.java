package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.event.EventStartEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class EventStartEventListener extends AbstractEventListener<EventStartEvent> implements MessageListener {

    public EventStartEventListener(ObjectMapper objectMapper,
                                   UserServiceClient userServiceClient,
                                   MessageBuilder<EventStartEvent> messageBuilder,
                                   List<NotificationService> notifications) {
        super(objectMapper, userServiceClient, messageBuilder, notifications);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventStartEvent eventStartEvent = handleEvent(message, EventStartEvent.class);
        String userMessage = getMessage(eventStartEvent, Locale.getDefault());

        eventStartEvent.userIds().forEach(userId -> sendNotification(userId, userMessage));
    }
}
