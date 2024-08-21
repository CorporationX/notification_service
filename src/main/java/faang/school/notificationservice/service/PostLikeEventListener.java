package faang.school.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class PostLikeEventListener extends AbstractEventListener<LikeEvent> implements MessageListener {

    public PostLikeEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationServices, List<MessageBuilder<LikeEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try{
            LikeEvent likeEvent = objectMapper.readValue(message.getBody(), LikeEvent.class);
            sendNotification(likeEvent.getAuthorPostId(), getMessage(likeEvent, Locale.UK));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
