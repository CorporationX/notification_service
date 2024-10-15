package faang.school.notificationservice.redis.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClientMock;
import faang.school.notificationservice.redis.event.FollowerEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {
    public FollowerEventListener(UserServiceClientMock userServiceClient,
                                 List<MessageBuilder<?>> messageBuilders,
                                 List<NotificationService> notificationServices,
                                 ObjectMapper objectMapper) {
        super(userServiceClient, messageBuilders, notificationServices, objectMapper, FollowerEvent.class);
    }

    @Override
    public void processEvent(FollowerEvent event) {
        MessageBuilder<FollowerEvent> messageBuilder = (MessageBuilder<FollowerEvent>) defineBuilder(FollowerEvent.class);
        String userMessage = messageBuilder.buildMessage(event, Locale.getDefault());
        sendNotification(event.followedId(), userMessage);
    }
}
