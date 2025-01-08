package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.RecommendationRequestedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationRequestedEvent> implements MessageListener {
    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                               UserServiceClient userServiceClient,
                                               List<NotificationService> notificationService,
                                               MessageBuilder<RecommendationRequestedEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationRequestedEvent event = mapMessage(message, RecommendationRequestedEvent.class);
        String text = getMessage(event, event.getReceiverId());
        sendMessage(event.getReceiverId(), text);
    }
}