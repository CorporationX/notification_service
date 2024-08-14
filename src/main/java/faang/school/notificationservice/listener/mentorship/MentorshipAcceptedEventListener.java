package faang.school.notificationservice.listener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.mentorship.request.MentorshipAcceptedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
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
                                           List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders,
                                           List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipAcceptedEvent.class, event -> {
            var notificationContent = getMessage(event, Locale.US);
            sendNotification(event.getRequesterId(), notificationContent);
        });
    }
}
