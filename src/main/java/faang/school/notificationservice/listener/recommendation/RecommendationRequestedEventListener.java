package faang.school.notificationservice.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationRequestedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class RecommendationRequestedEventListener
        extends AbstractEventListener<RecommendationRequestedEvent>
        implements MessageListener {
    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                                UserServiceClient userServiceClient,
                                                List<NotificationService> notificationServices,
                                                List<MessageBuilder<RecommendationRequestedEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationRequestedEvent.class, requestedEvent -> {
            String text = getMessage(requestedEvent, Locale.getDefault());
            sendNotification(requestedEvent.getReceiverId(), text);
        });
    }
}
