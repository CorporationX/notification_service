package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {
    public FollowerEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServiceList, List<MessageBuilder<FollowerEvent>> messageBuilders) {
        super(userServiceClient, notificationServiceList, messageBuilders, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, followerEvent -> {
            UserDto follower = userServiceClient.getUser(followerEvent.getFollowerId());
            String text = getMessage(followerEvent, Locale.UK);
            sendNotification(followerEvent.getFolloweeId(), text);
        });
    }
}
