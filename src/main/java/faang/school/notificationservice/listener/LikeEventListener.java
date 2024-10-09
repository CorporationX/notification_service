package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.LikeEventDto;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class LikeEventListener extends AbstractEventListener<LikeEventDto> implements MessageListener {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             MessageBuilder<LikeEventDto> messageBuilder,
                             List<NotificationService> notificationServices) {
        super(objectMapper,
                userServiceClient,
                messageBuilder,
                notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LikeEventDto likeEvent = handleEvent(message, LikeEventDto.class);
        String text = getMessage(likeEvent, Locale.getDefault());
        sendNotification(likeEvent.postAuthorId(), text);
    }
}
