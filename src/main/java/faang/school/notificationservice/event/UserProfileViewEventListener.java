package faang.school.notificationservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.UserProfileViewEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class UserProfileViewEventListener extends AbstractEventListener<UserProfileViewEvent> implements MessageListener {
    public UserProfileViewEventListener(ObjectMapper objectMapper,
                                        UserServiceClient userServiceClient,
                                        MessageBuilder<UserProfileViewEvent> messageBuilder,
                                        List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, UserProfileViewEvent.class, (event) -> {
            Long visitedUserId = event.visitedUserId();
            String notificationMessage = getMessage(event, Locale.ENGLISH);
            sendNotification(visitedUserId, notificationMessage);
        });
    }
}
