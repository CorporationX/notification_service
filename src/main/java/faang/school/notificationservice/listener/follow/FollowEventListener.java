package faang.school.notificationservice.listener.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.event.FollowEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.UserService;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class FollowEventListener extends AbstractEventListener<FollowEvent> {
    public FollowEventListener(
            ObjectMapper objectMapper,
            UserService userService,
            List<NotificationService> notificationServices,
            List<MessageBuilder<FollowEvent>> messageBuilders
    ) {
        super(objectMapper, userService, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FollowEvent event = getEventFromBytes(message.getBody(), FollowEvent.class);
        String notificationMessage = getMessage(event, Locale.getDefault());
        sendNotification(event.getFolloweeId(), notificationMessage);
    }
}
