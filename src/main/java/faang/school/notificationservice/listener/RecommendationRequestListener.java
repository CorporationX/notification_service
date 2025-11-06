package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import java.util.List;
import java.util.Locale;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RecommendationRequestListener extends AbstractEventListener<RecommendationRequestEvent>
        implements MessageListener {

    public RecommendationRequestListener(ObjectMapper objectMapper,
                                         UserServiceClient userServiceClient,
                                         List<NotificationService> notificationServices,
                                         List<MessageBuilder<RecommendationRequestEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationRequestEvent.class, event -> {
            String text = getMessage(event, Locale.UK);
            sendNotification(event.recommenderId(), text);
        });
    }
}
