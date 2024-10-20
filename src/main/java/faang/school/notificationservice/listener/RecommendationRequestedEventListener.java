package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.RecommendationRequestedEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationRequestedEvent>
        implements MessageListener {

    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                                UserServiceClient userServiceClient,
                                                MessageBuilder<RecommendationRequestedEvent> messageBuilder,
                                                List<NotificationService> notifications) {
        super(objectMapper, userServiceClient, messageBuilder, notifications);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationRequestedEvent recommendationReqEvent = handleEvent(message, RecommendationRequestedEvent.class);
        String userMessage = getMessage(recommendationReqEvent, Locale.getDefault());
        sendNotification(recommendationReqEvent.receiverId(), userMessage);
    }
}
