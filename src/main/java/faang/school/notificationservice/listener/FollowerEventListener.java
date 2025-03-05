package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_SUBSCRIPTION;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {


    public FollowerEventListener(List<MessageBuilder<FollowerEvent>> messageBuilders,
                                       ObjectMapper objectMapper,
                                       UserServiceClient userServiceClient,
                                       List<NotificationService> notificationServices,
                                       UserContext userContext) {
        super(messageBuilders, objectMapper.registerModule(new JavaTimeModule()), userServiceClient, notificationServices, userContext);
    }

    @Override
    public EventType getEventType() {
        return EVENT_TYPE_SUBSCRIPTION;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, event -> {
            String messageText = getMessage(event.followeeId(), event);
            sendNotification(event.followeeId(), messageText);
        });
    }


}
