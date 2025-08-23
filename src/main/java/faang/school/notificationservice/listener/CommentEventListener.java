package faang.school.notificationservice.listener;

import faang.school.notificationservice.events.CommentEvent;
import faang.school.notificationservice.service.CommentNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CommentEventListener.class);

    @Autowired
    private CommentNotificationService commentNotificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            CommentEvent event = objectMapper.readValue(messageBody, CommentEvent.class);

            logger.info("Received comment event for post {} by user {}",
                    event.postId(), event.commentAuthorId());

            commentNotificationService.sendCommentNotification(event);

        } catch (Exception e) {
            logger.error("Error processing comment event", e);
        }
    }
}
