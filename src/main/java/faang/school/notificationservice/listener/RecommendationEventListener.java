package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.event.RecommendationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;
import java.util.List;
import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_RECOMMENDATION;

@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> {

    public RecommendationEventListener(List<MessageBuilder<RecommendationEvent>> messageBuilders,
                                       ObjectMapper objectMapper,
                                       UserServiceClient userServiceClient,
                                       List<NotificationService> notificationServices,
                                       UserContext userContext) {
        super(messageBuilders, objectMapper, userServiceClient, notificationServices, userContext);
    }

    public EventType getEventType() {

        return EVENT_TYPE_RECOMMENDATION;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationEvent.class, event -> {
            String messageText = getMessage(event.receiverId(), event);
            sendNotification(event.receiverId(), messageText);
        });
    }
}
