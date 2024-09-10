package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikePostEventListener extends AbstractEventListener<LikePostEvent> implements MessageListener {

    public LikePostEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<LikePostEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikePostEvent likePostEvent = handleMessage(message, LikePostEvent.class);
        UserDto user = userServiceClient.getUser(likePostEvent.getPostAuthorId());
        String msg = getMessage(likePostEvent, user.getPreferredLocale());
        sendNotification(user, msg);
    }
}
