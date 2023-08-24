package faang.school.notificationservice.event_listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> implements MessageListener {

    @Autowired
    public RecommendationReceivedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders, List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationReceivedEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), RecommendationReceivedEvent.class);
        } catch (IOException e) {
            log.error("Error reading RecommendationReceivedEvent from message body: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        String text = getMessage(event, Locale.getDefault());

        sendNotification(event.getRecipientId(), text);
    }
}

