package faang.school.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.client.UserServiceClient;
import faang.school.notificationservice.model.event.MentorshipOfferedEvent;
import faang.school.notificationservice.messaging.MessageBuilder;
import faang.school.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> implements MessageListener {

    public MentorshipOfferedEventListener(
            ObjectMapper objectMapper,
            UserServiceClient userServiceClient,
            MessageBuilder<MentorshipOfferedEvent> messageBuilder,
            List<NotificationService> notifications
    ) {
        super(objectMapper, userServiceClient, messageBuilder, notifications);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
            MentorshipOfferedEvent mentorshipOfferedEvent = handleEvent(message, MentorshipOfferedEvent.class);
            String userMessage = getMessage(mentorshipOfferedEvent, Locale.getDefault());

            sendNotification(mentorshipOfferedEvent.receiverId(), userMessage);
    }
}
