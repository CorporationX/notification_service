package faang.school.notificationservice.eventlistener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.recommendation.RecommendationReceivedEvent;
import faang.school.notificationservice.eventlistener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> implements MessageListener {


    public RecommendationReceivedEventListener(ObjectMapper objectMapper,
                                               UserServiceClient userServiceClient,
                                               List<NotificationService> notificationServices,
                                               List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationReceivedEvent.class, event -> {
            String text = getMessage(event, Locale.getDefault());
            sendNotification(event.getReceiverId(), text);
        });
    }
}
