package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipOfferedEvent event = objectMapper.readValue(message.getBody(), MentorshipOfferedEvent.class);
            String notification = getMessage(event, Locale.UK); // here locale should take from user
            sendNotification(event.getMentorId(), notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
