package faang.school.notificationservice.listener;

import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.exception.EventProcessingException;
import faang.school.notificationservice.service.CommentNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener implements MessageListener {

    private final CommentNotificationService commentNotificationService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            CommentEvent event = objectMapper.readValue(messageBody, CommentEvent.class);

            log.info("Received comment event for post {} by user {}",
                    event.postId(), event.commentAuthorId());

            commentNotificationService.sendCommentNotification(event);

        } catch (Exception e) {
            String rawMessage = new String(message.getBody());
            throw new EventProcessingException("Failed to process comment event: " + rawMessage, e);
        }
    }
}
