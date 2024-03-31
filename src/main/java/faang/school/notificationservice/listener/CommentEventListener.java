package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto> {

    @Autowired
    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<MessageBuilder<CommentEventDto>> messageBuilders,
                                List<NotificationService> notificationServices,
                                UserContext userContext) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices, userContext);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, CommentEventDto.class, event -> {
            String text = getMessage(event, Locale.ENGLISH);
            UserDto user = userServiceClient.getUser(event.getAuthorPostId());
            sendNotification(user, text);
        });
    }
}