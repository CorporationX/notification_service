package faang.school.notificationservice.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.message.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.message.event.PostLikeEvent;
import faang.school.notificationservice.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PostLikeEventListener extends AbstractEventListener<PostLikeEvent> implements MessageListener {
    protected PostLikeEventListener(ObjectMapper mapper,
                                    UserServiceClient userServiceClient,
                                    List<NotificationService> notificationServices,
                                    MessageBuilder<PostLikeEvent> messageBuilder) {
        super(mapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostLikeEvent.class, PostLikeEvent::getReceiverId);
    }
}