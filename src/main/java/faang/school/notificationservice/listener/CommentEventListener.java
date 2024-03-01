package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message_builder.CommentMessageBuilder;
import faang.school.notificationservice.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {

    UserServiceClient userServiceClient;

    public CommentEventListener(UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEventDto>> messageBuilders,
                                List<NotificationService> notificationService,
                                ObjectMapper mapper) {
        super(userServiceClient, messageBuilders, notificationService, mapper);
        this.userServiceClient = userServiceClient;
    }

    public void onMessage(Message message, byte[] pattern) {
        CommentEventDto event = getEvent(message, CommentEventDto.class);
        String notifyMessage = getMessage(CommentMessageBuilder.class, event, Locale.ENGLISH);

        UserDto user = userServiceClient.getUser(event.getAuthorPostId());
        sendNotification(user, notifyMessage);
    }
}