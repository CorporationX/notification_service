package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public LikeEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<MessageBuilder<LikeEvent>> messageBuilders,
            List<NotificationService> notificationServices
    ) {
        super(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEvent.class, event -> {
            UserDto authorLike = userServiceClient.getUser(event.getAuthorLikeId());
            log.info("LikeEventListener authorLike: {}", authorLike);
            String text = getMessage(event, Locale.UK);
            sendNotification(event.getAuthorPostId(), text);
        });
    }
}
