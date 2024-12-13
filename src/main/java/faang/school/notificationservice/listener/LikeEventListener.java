package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.like.LikeEvent;
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
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class,
                event -> {
                    UserDto likeUser = userServiceClient.getUser(event.likeUserId());
                    String text = getMessage(event, Locale.getDefault()) + likeUser.getUsername();
                    sendNotification(event.postUserId(), text);
                });
    }
}
