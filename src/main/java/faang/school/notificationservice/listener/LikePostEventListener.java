package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.LikePostResponseDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class LikePostEventListener extends AbstractEventListener<LikePostResponseDto> {

    public LikePostEventListener(ObjectMapper objectMapper,
                                 UserServiceClient userServiceClient,
                                 List<MessageBuilder<LikePostResponseDto>> messageBuilders,
                                 List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handlerEvent(message, LikePostResponseDto.class);
    }

    @Override
    protected void processEvent(LikePostResponseDto event) {
        String text = getMessage(event, Locale.getDefault());
        sendNotification(event.getAuthorPostId(), text);
    }
}
