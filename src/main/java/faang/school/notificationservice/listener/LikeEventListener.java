package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.redis.LikeEventDto;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEventDto> implements MessageListener {


    public LikeEventListener(ObjectMapper objectMapper,
                             List<NotificationService> notificationServices,
                             UserServiceClient userServiceClient,
                             List<MessageBuilder<LikeEventDto>> messageBuilders) {
        super(objectMapper, notificationServices, userServiceClient, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto likeEventDto = getEvent(getMessageBody(message), LikeEventDto.class);

        UserDto contentAuthor = (likeEventDto.getPostId() != 0) ?
                userServiceClient.getUser(likeEventDto.getPostAuthor()) :
                (likeEventDto.getCommentAuthor() != 0) ?
                        userServiceClient.getUser(likeEventDto.getCommentAuthor()) :
                        new UserDto();

        sendNotification(contentAuthor, getMessage(likeEventDto, Locale.UK));
    }
}
