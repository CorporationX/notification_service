package faang.school.notificationservice.listener.recommendationReceived;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.recommendationReceived.RecommendationReceivedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class RecommendationReceivedListener extends AbstractEventListener<RecommendationReceivedEvent> implements MessageListener {
    public RecommendationReceivedListener(ObjectMapper objectMapper,
                                          UserServiceClient userServiceClient,
                                          List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders,
                                          List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationReceivedEvent.class, event -> {
            sendNotification(event.getReceivedId(), getMessage(event, Locale.UK));
        });
    }
}