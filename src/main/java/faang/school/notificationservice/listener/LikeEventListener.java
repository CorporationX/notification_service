package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.redis.LikeEventDto;
import faang.school.notificationservice.messageBuilder.MessageBuilder;
import faang.school.notificationservice.sender.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component

public class LikeEventListener extends AbstractEventListener<LikeEventDto, String> implements MessageListener {

    public LikeEventListener(ObjectMapper objectMapper,
                             List<NotificationService> notificationServices,
                             UserServiceClient userServiceClient,
                             List<MessageBuilder<LikeEventDto, String>> messageBuilders) {
        super(objectMapper, notificationServices, userServiceClient, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto likeEventDto = getEvent(getMessageBody(message), LikeEventDto.class);
        long contentAuthor;
        String contentName;
        if (likeEventDto.getPostId() != 0) {
            contentAuthor = likeEventDto.getPostAuthor();
            contentName = "Post: " + likeEventDto.getPostId();
        } else {
            contentAuthor = likeEventDto.getCommentAuthor();
            contentName = "Comment: " + likeEventDto.getCommentId();
        }
        sendNotification(contentAuthor, getMessage(likeEventDto.getClass(), Locale.getDefault(), likeEventDto, contentName));

    }
}
