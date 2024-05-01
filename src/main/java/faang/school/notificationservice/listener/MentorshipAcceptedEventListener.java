package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.messagebroker.MentorshipAcceptedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> implements MessageListener {

    public MentorshipAcceptedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient, List<NotificationService> notificationService, MessageBuilder<MentorshipAcceptedEvent> messageBuilders) {
        super(objectMapper, userServiceClient, notificationService, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, MentorshipAcceptedEvent.class);
    }
}
