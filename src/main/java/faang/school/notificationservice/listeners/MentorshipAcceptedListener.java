package faang.school.notificationservice.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.events.MentorshipAcceptedEvent;
import faang.school.notificationservice.listeners.general.AbstractEventListener;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.List;
import java.util.Locale;

@Slf4j
public class MentorshipAcceptedListener extends AbstractEventListener<MentorshipAcceptedEvent> implements MessageListener {

    public MentorshipAcceptedListener(ObjectMapper objectMapper,
                                      UserServiceClient userServiceClient,
                                      List<NotificationService> notificationServices,
                                      List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message from channel {}: {}", channel, body);

        MentorshipAcceptedEvent mentorshipAcceptedEvent = constructEvent(message.getBody(), MentorshipAcceptedEvent.class);
        sendMessage(mentorshipAcceptedEvent, mentorshipAcceptedEvent.getReceiverId(), Locale.US);
    }
}
