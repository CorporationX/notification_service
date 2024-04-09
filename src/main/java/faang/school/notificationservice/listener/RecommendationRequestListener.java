package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.builder.MessageBuilder;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.FollowerEvent;
import faang.school.notificationservice.dto.RecommendationRequestEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class RecommendationRequestListener extends AbstractEventListener<RecommendationRequestEvent> implements MessageListener {

    public RecommendationRequestListener(ObjectMapper objectMapper,
                                         UserServiceClient userServiceClient,
                                         List<NotificationService> notificationServiceList,
                                         List<MessageBuilder<RecommendationRequestEvent>> messageBuilders) {
        super(userServiceClient, notificationServiceList, messageBuilders, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationRequestEvent.class, recommendationRequestEvent -> {
            String text = getMessage(recommendationRequestEvent, Locale.UK);
            sendNotification(recommendationRequestEvent.getReceiverId(), text);
        });
    }
}
