package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.PostServiceClient;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {


    public CommentEventListener(ObjectMapper objectMapper, List<NotificationService> notificationServiceList,
                                List<MessageBuilder<CommentEventDto>> messageBuilders, UserServiceClient userServiceClient, PostServiceClient postServiceClient) {
        super(objectMapper, notificationServiceList, messageBuilders, userServiceClient, postServiceClient);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messagesHeader");
            CommentEventDto event = objectMapper.readValue(message.getBody(), CommentEventDto.class);
            String messageToSend = getMessage(event, Locale.getDefault());
            String messagesHeader = bundle.getString("comment.new.header");
            sendNotification(event.getPostId(), messageToSend, messagesHeader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}