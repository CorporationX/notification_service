package faang.school.notificationservice.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.dto.event.CommentEvent;
import faang.school.notificationservice.service.NotificationService;
import faang.school.notificationservice.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class CommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {

    @Value("${spring.data.redis.channel.comment}")
    private String commentChannel;

    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEvent>> messageBuilders,
                                List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, commentEvent -> {
            String text = getMessage(commentEvent, Locale.getDefault());
            sendNotification(commentEvent.postAuthorId(), text);
            log.info("Sent comment notification to user {}, text: {}", commentEvent.postAuthorId(), text);
        });
    }
}

