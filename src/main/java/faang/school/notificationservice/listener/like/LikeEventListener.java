package faang.school.notificationservice.listener.like;

import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent> {

    public LikeEventListener(List<MessageBuilder<LikeEvent>> messageBuilders, List<NotificationService> notificationServices) {
        super(messageBuilders, notificationServices);
    }

    public LikeEvent getEventFromBytes(byte[] body) {
        try {
            return objectMapper.readValue(body, LikeEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onMessage(Message message, byte[] body) {
        LikeEvent likeEvent = getEventFromBytes(message.getBody());
        String eventMessage = getMessage(likeEvent, Locale.getDefault());
        sendNotification(likeEvent.getUserId(), eventMessage);
    }
}
