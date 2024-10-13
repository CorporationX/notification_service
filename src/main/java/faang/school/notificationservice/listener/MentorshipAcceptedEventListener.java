package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.model.event.MentorshipAcceptedEvent;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent>
        implements MessageListener {


    public MentorshipAcceptedEventListener(ObjectMapper objectMapper,
                                           UserServiceClient userServiceClient,
                                           MessageBuilder<MentorshipAcceptedEvent> messageBuilder,
                                           List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilder, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipAcceptedEvent eventStartEvent = handleEvent(message, MentorshipAcceptedEvent.class);
        String userMessage = getMessage(eventStartEvent, Locale.getDefault());
        sendNotification(eventStartEvent.requesterId(), userMessage);
    }
}
