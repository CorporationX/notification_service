package faang.school.notificationservice.messages.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.LikeEventDto;
import faang.school.notificationservice.messages.builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class LikeEventListener extends AbstractEventListener<LikeEventDto> implements MessageListener {

    public LikeEventListener(UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             List<MessageBuilder<LikeEventDto>> messageBuilders,
                             ObjectMapper objectMapper) {
        super(userServiceClient, notificationServices, messageBuilders, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, LikeEventDto.class, likeEvent -> {
            String text = getMessage(likeEvent, Locale.UK);
            sendNotification(likeEvent.getAuthorId(), text);
        });
    }
}
