package faang.school.notificationservice.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.service.UserServiceClient;
import faang.school.notificationservice.dto.redis.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.message_builder.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent>
        implements MessageListener {

    @Autowired
    public RecommendationReceivedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                               MessageBuilder<RecommendationReceivedEvent> messageBuilder,
                                               List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        RecommendationReceivedEvent event = mapEvent(message, RecommendationReceivedEvent.class);
        String text = getMessage(event, Locale.getDefault());
        sendNotification(event.getRecipientId(), text);
    }
}

