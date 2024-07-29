package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEvent>{

    public LikeEventListener(ObjectMapper objectMapper, List<NotificationService> notificationServiceList,
                             List<MessageBuilder<LikeEvent>> messageBuilders,
                             UserServiceClient userServiceClient, PostServiceClient postServiceClient) {
        super(objectMapper, notificationServiceList, messageBuilders, userServiceClient, postServiceClient);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messagesHeader");
            LikeEvent event = objectMapper.readValue(message.getBody(), LikeEvent.class);
            String messageToSend = getMessage(event, Locale.getDefault());
            String messagesHeader = bundle.getString("like.new.header");
            sendNotification(event.getPostId(), messageToSend, messagesHeader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
