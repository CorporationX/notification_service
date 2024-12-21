package faang.school.notificationservice.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.messaging.MentorshipOfferedMessageBuilder;
import faang.school.notificationservice.service.NotificationSender;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> {
    private final NotificationSender notificationSender;
    private final MentorshipOfferedMessageBuilder builder;

    public MentorshipOfferedEventListener(ObjectMapper objectMapper, NotificationSender notificationSender, MentorshipOfferedMessageBuilder mentorshipOfferedMessageBuilder) {
        super(objectMapper);
        this.notificationSender = notificationSender;
        this.builder = mentorshipOfferedMessageBuilder;
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEvent.class, event
                -> {
            String str = builder.buildMessage(new MentorshipOfferedEvent(event.idRequest(), event.idAuthor(), event.idReceiver()), Locale.ENGLISH);
            notificationSender.sendNotification(event.idReceiver(), str);
        });
    }
}