package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.publisher_events.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> {

    public RecommendationReceivedEventListener(
            ObjectMapper objectMapper,
            List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders,
            UserServiceClient userServiceClient,
            List<NotificationService> notificationServices) {
        super(objectMapper, messageBuilders, userServiceClient, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationReceivedEvent event = getEvent(message, RecommendationReceivedEvent.class);
        String text = getMessage(event, Locale.UK);
        sendNotification(event.getReceiverId(), text);
    }
}
