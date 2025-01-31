package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.message.event.CommentEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostCommentEventListener extends AbstractEventListener<CommentEvent> implements MessageListener {


    protected PostCommentEventListener(ObjectMapper mapper, UserServiceClient userServiceClient,
                                       List<NotificationService> notificationServices,
                                       MessageBuilder<CommentEvent> messageBuilder) {
        super(mapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, CommentEvent::getReceiverId);
    }
}
