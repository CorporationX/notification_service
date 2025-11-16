package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class RecommendationRequestListener extends AbstractEventListener<RecommendationRequestEvent> {

    public RecommendationRequestListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            List<MessageBuilder<RecommendationRequestEvent>> messageBuilders,
            Class<RecommendationRequestEvent> eventType) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, eventType);
    }

    @Override
    protected void processEvent(RecommendationRequestEvent event) {
        String text = getMessage(event, Locale.UK);
        sendNotification(event.recommenderId(), text);
    }
}
