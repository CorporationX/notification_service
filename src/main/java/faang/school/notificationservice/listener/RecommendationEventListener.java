package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.config.redis.Channels;
import faang.school.notificationservice.event.RecommendationEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_RECOMMENDATION;

@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> {

    private final Channels channels;

    public RecommendationEventListener(List<MessageBuilder<RecommendationEvent>> messageBuilders,
                                       ObjectMapper objectMapper,
                                       UserServiceClient userServiceClient,
                                       List<NotificationService> notificationServices,
                                       UserContext userContext, Channels channels) {
        super(messageBuilders, objectMapper, userServiceClient, notificationServices, userContext);
        this.channels = channels;
    }

    public EventType getEventType() {

        return EVENT_TYPE_RECOMMENDATION;
    }

    @Override
    public String getTopicName() {
        return channels.getRecommendationChannel();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationEvent.class, event -> {
            String messageText = getMessage(event);
            sendNotification(event.receiverId(), messageText);
        });
    }
}
