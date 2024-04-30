package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    public FollowerEventListener(MessageBuilder<FollowerEvent> messageBuilder,
                                 List<NotificationService> notificationServices,
                                 UserServiceClient userServiceClient,
                                 ObjectMapper objectMapper) {
        super(messageBuilder, notificationServices, userServiceClient, objectMapper, FollowerEvent.class);
    }

    @Override
    protected void sendSpecifiedNotification(FollowerEvent event) {
        sendNotification(event.getFolloweeId(), getMessage(event));
    }
}
