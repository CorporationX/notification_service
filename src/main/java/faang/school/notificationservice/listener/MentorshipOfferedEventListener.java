package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.config.redis.Channels;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

import static faang.school.notificationservice.listener.EventType.EVENT_TYPE_RECOMMENDATION;

@Slf4j
@Component
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> {

    private final Channels channels;

    public MentorshipOfferedEventListener(List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders,
                                          ObjectMapper objectMapper,
                                          UserServiceClient userServiceClient,
                                          List<NotificationService> notificationServices,
                                          UserContext userContext, Channels channels) {

        super(messageBuilders, objectMapper, userServiceClient, notificationServices, userContext);
        this.channels = channels;
    }

    @Override
    public EventType getEventType() {
        return EVENT_TYPE_RECOMMENDATION;
    }

    @Override
    public String getTopicName() {
        return channels.getRecommendationMentorshipOffered();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEvent.class, event -> {
            String messageText = getMessage(event);
            sendNotification(event.getMentorId(), messageText);
        });
    }
}