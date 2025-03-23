package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.config.context.UserContext;
import faang.school.notificationservice.config.redis.Channels;
import faang.school.notificationservice.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> {

    private final Channels channels;

    public MentorshipAcceptedEventListener(List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders,
                                           ObjectMapper objectMapper,
                                           UserServiceClient userServiceClient,
                                           List<NotificationService> notificationServices,
                                           UserContext userContext,
                                           Channels channels) {

        super(messageBuilders, objectMapper, userServiceClient, notificationServices, userContext);
        this.channels = channels;
    }

    @Override
    public EventType getEventType() {
        return EventType.EVENT_TYPE_MENTORSHIP_ACCEPTED;
    }

    @Override
    public String getTopicName() {
        return channels.getMentorshipAcceptedChannel();
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipAcceptedEvent.class, event -> {
            String messageText = getMessage(event);
            sendNotification(event.mentorId(), messageText);
        });
    }
}
