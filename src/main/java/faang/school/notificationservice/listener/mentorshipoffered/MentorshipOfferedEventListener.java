package faang.school.notificationservice.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.redisevent.MentorshipOfferedEvent;
import faang.school.notificationservice.service.NotificationSender;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> implements MessageListener {
    public MentorshipOfferedEventListener(ObjectMapper objectMapper, NotificationSender notificationSender,
                                          MentorshipOfferedMessageBuilder messageBuilder) {
        super(objectMapper, notificationSender, messageBuilder);
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEvent.class, event -> {
            String messageText = getMessage(event, Locale.ENGLISH);
            notificationSender.sendNotification(event.idReceiver(), messageText);
        });
    }
}