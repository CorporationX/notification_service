package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.CommentEventDto;
import faang.school.notificationservice.messages.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEventDto>{
    public CommentEventListener(ObjectMapper objectMapper,
                                UserServiceClient userServiceClient,
                                List<NotificationService> notificationServices,
                                MessageBuilder<CommentEventDto> messageBuilder) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilder);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        CommentEventDto commentEventDto = fromJsonToObject(message, CommentEventDto.class);
        String messageForSend = getMessage(commentEventDto, Locale.ENGLISH);
        sendNotification(commentEventDto.getAuthorPostId(), messageForSend);
    }
}
