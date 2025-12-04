package faang.school.notificationservice.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.builder.MessageBuilder;
import faang.school.notificationservice.service.notification.NotificationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class RecommendationRequestListener extends AbstractEventListener<RecommendationRequestEvent> {

    public RecommendationRequestListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices,
            List<MessageBuilder<RecommendationRequestEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders, RecommendationRequestEvent.class);
    }

    @Override
    protected void processEvent(RecommendationRequestEvent event) {
        String text = getMessage(event, Locale.UK);
        sendNotification(event.recommenderId(), text);
    }
}
