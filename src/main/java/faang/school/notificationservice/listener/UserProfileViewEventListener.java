package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.event.UserProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_PROFILE_VIEW;

@Slf4j
@Component
public class UserProfileViewEventListener extends AbstractEventListener<UserProfileViewEvent>{
    public UserProfileViewEventListener(List<MessageBuilder<UserProfileViewEvent>> messageBuilders,
                                       ObjectMapper objectMapper,
                                       UserServiceClient userServiceClient,
                                       List<NotificationService> notificationServices,
                                       UserContext userContext) {
        super(messageBuilders, objectMapper, userServiceClient, notificationServices, userContext);
    }

    public EventType getEventType() {

        return EVENT_TYPE_PROFILE_VIEW;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, UserProfileViewEvent.class, (event) -> {
            Long visitedUserId = event.visitedUserId();
            String notificationMessage = getMessage(event);
            sendNotification(visitedUserId, notificationMessage);
        });
    }
}
