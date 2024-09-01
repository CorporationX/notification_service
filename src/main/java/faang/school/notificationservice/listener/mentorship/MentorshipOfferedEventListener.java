package faang.school.notificationservice.listener.mentorship;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.event.mentorship.request.MentorshipOfferedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent>
        implements MessageListener {
    public MentorshipOfferedEventListener(ObjectMapper objectMapper,
                                          UserServiceClient userServiceClient,
                                          List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders,
                                          List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, messageBuilders, notificationServices);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEvent.class, event -> {
            var notificationContent = getMessage(event, Locale.US);
            sendNotification(event.getReceiverId(), notificationContent);
        });
    }
}
