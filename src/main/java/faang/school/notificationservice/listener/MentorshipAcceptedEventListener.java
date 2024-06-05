package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.dto.MentorshipAcceptedEvent;
import faang.school.notificationservice.service.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Setter
@Component
public class MentorshipAcceptedEventListener extends AbstractEventListener<MentorshipAcceptedEvent> implements MessageListener {

    public MentorshipAcceptedEventListener(ObjectMapper objectMapper, UserServiceClient userServiceClient,
                                           List<NotificationService> notificationServices,
                                           List<MessageBuilder<MentorshipAcceptedEvent>> messageBuilders) {
        super(objectMapper, userServiceClient, notificationServices, messageBuilders);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message from channel {}: {}", new String(message.getChannel()), new String(message.getBody()));
        handleEvent(message, MentorshipAcceptedEvent.class,
                event -> sendNotification(event.getActorId(), getMessage(event, Locale.ENGLISH)));
    }
}
