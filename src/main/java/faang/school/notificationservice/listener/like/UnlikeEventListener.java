package faang.school.notificationservice.listener.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UnlikeEventListener extends AbstractLikeEventListener{

    public UnlikeEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            Map<Class<?>, MessageBuilder<?>> messageBuilderMap) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilderMap);
    }
}
