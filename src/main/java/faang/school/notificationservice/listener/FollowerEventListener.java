package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
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

    public FollowerEventListener(
            ObjectMapper objectMapper,
            UserServiceClient serviceClient,
            List<MessageBuilder<FollowerEvent>> messageBuilders,
            List<NotificationService> notificationServices) {
        super(objectMapper, serviceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, event -> {
            UserDto followee = userClient.getUser(event.followeeId());
            Locale followeeLocale = Locale.ENGLISH;
            String notificationMessage = getMessage(event, followeeLocale);
            sendNotification(followee, notificationMessage);
        });
    }
}
