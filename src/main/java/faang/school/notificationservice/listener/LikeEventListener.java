package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("LikeEventListener listens for messages");
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);
            UserDto user = userServiceClient.getUser(event.getAuthorLikeId());
            String text = getMessage(user, event);
            sendNotification(user, text);
            log.info("LikeEventListener sent a message. Author like id: {}, post id: {}",
                    event.getAuthorLikeId(), event.getAuthorPostId());
        } catch (IOException exception) {
            log.info("Failed to deserialize like event", exception);
        }
    }
}
