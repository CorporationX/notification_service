package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.MentorshipOfferedEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;
import java.util.Locale;

public class MentorshipRequestListener extends AbstractEventListener<MentorshipOfferedEvent> implements MessageListener {

    public MentorshipRequestListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                     List<MessageBuilder<MentorshipOfferedEvent>> messageBuilders,
                                     List<NotificationService> notificationServices) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipOfferedEvent event = constructEvent(message.getBody(), MentorshipOfferedEvent.class);
        sendMessage(event, event.getMentorId(), Locale.US);
    }
}
