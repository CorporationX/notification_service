package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.redis.RecommendationRequestEvent;
import faang.school.notificationservice.message.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Component
public class RecommendationRequestedEventListener extends AbstractEventListener<RecommendationRequestEvent> implements MessageListener {

    public RecommendationRequestedEventListener(ObjectMapper objectMapper,
                                                UserServiceClient userServiceClient,
                                                List<MessageBuilder<RecommendationRequestEvent>> messageBuilders,
                                                List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationRequestEvent event;

        try {
            event = objectMapper.readValue(message.getBody(), RecommendationRequestEvent.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendNotification(event.getReceiverId(), buildMessage(event, Locale.UK));
    }

}
