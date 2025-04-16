package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.like.LikeEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LikeEventListener extends AbstractLikeEventListener {

    public LikeEventListener(ObjectMapper objectMapper,
                             UserServiceClient userServiceClient,
                             List<NotificationService> notificationServices,
                             MessageBuilder<LikeEvent> likeMessageBuilder) {
        super(objectMapper, userServiceClient, notificationServices,
                Map.of(LikeEvent.class, likeMessageBuilder));
    }
}
