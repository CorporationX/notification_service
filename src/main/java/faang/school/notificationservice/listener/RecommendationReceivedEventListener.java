package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.event.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> implements MessageListener {
    public RecommendationReceivedEventListener(ObjectMapper objectMapper,
                                               UserServiceClient userServiceClient,
                                               List<NotificationService> notificationService,
                                               MessageBuilder<RecommendationReceivedEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationReceivedEvent event = mapMessage(message, RecommendationReceivedEvent.class);
        String text = getMessage(event, event.getReceiverId());
        sendMessage(event.getReceiverId(), text);
    }
}
