package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.like.LikeEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class LikeEventListener extends AbstractEventListener<LikeEventDto> implements MessageListener {

    private final ObjectMapper objectMapper;

    public LikeEventListener(UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEventDto>> messageBuilders,
                             ObjectMapper objectMapper) {
        super(userServiceClient, notificationServices, messageBuilders);
        this.objectMapper = objectMapper;
    }

    public void handleLikeEvent(String message) {
        try {
            LikeEventDto event = objectMapper.readValue(message, LikeEventDto.class);
            String localizedMessage = getMessage(event, Locale.JAPAN);
            sendNotification(event.getPostAuthorId(), localizedMessage);
        } catch (Exception e) {
            log.error("Failed to handle LikeEvent message", e);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

    }
}
