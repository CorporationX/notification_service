package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.RecommendationReceivedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component("recommendationReceivedEventListener")
public class RecommendationReceivedEventListener extends AbstractEventListener<RecommendationReceivedEvent> {

    public RecommendationReceivedEventListener(List<NotificationService> notificationServices,
                                               List<MessageBuilder<RecommendationReceivedEvent>> messageBuilders,
                                               UserServiceClient userServiceClient,
                                               ObjectMapper objectMapper) {
        super(notificationServices, messageBuilders, userServiceClient, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.debug("Handling event from {}", new String(message.getChannel()));
        RecommendationReceivedEvent event = handleEvent(message, RecommendationReceivedEvent.class);
        String answerMessage = getMessage(event, Locale.ENGLISH);
        sendNotification(event.recipientId(), answerMessage);
        log.debug("Sent notification to user ID: {}", event.recipientId());
    }
}