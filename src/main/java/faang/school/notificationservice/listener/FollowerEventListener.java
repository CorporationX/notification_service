package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.follower.FollowerEvent;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    public FollowerEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<NotificationService> notificationServices,
                                 List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, FollowerEvent.class);
    }

    @Override
    protected void sendSpecifiedNotification(FollowerEvent event) {
        sendNotification(event.getFolloweeId(), getMessage(event, Locale.ENGLISH));
    }
}
