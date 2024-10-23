package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component("likeEventListener")
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    public LikeEventListener(List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEvent>> messageBuilders,
                             UserServiceClient userServiceClient,
                             ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEvent likeEvent = handleEvent(message, LikeEvent.class);
        String text = getMessage(likeEvent, Locale.UK);
        log.debug("Generated notification text: {}", text);
        sendNotification(likeEvent.postAuthorId(), text);
        log.debug("Notification sent successfully to author ID: {}", likeEvent.postAuthorId());
    }
}
