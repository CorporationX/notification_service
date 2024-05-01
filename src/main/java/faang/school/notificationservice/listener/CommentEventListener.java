package faang.school.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.CommentEvent;
import faang.school.notificationservice.messagebuilder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    public CommentEventListener(MessageBuilder<CommentEvent> messageBuilder,
                                List<NotificationService> notificationServices,
                                UserServiceClient userServiceClient,
                                ObjectMapper objectMapper) {
        super(messageBuilder, notificationServices, userServiceClient, objectMapper, CommentEvent.class);
    }

    @KafkaListener(topics = "${spring.data.kafka.channels.comment-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(String event) {
        try {
            CommentEvent commentEvent = objectMapper.readValue(event, CommentEvent.class);
            sendNotification(commentEvent.getPostAuthorId(), getMessage(commentEvent));
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    protected void sendSpecifiedNotification(CommentEvent event) {
        // Потому что кафку и редис одновременно используем...
    }
}
