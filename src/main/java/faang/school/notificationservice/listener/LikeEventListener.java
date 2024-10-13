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

import java.util.Locale;
import java.util.Map;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             MessageBuilder<LikeEvent> messageBuilder,
                             Map<UserDto.PreferredContact, NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.debug("Received message: {}", message);
        handleEvent(message, LikeEvent.class, likeEvent -> {
            String text = getMessage(likeEvent, Locale.UK);
            log.debug("Generated notification text: {}", text);
            try {
                sendNotification(likeEvent.postAuthorId(), text);
                log.info("Notification sent successfully to author ID: {}", likeEvent.postAuthorId());
            } catch (Exception e) {
                log.error("Failed to send notification to author ID: {}", likeEvent.postAuthorId(), e);
            }
        });
    }
}
