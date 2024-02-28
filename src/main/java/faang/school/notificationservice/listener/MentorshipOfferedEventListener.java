package faang.school.notificationservice.listener;

import faang.school.notificationservice.dto.MentorshipOfferedEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> {


    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipOfferedEvent event = getEvent(message.getBody(), MentorshipOfferedEvent.class);
        String notification = getMessage(event);
        sendNotification(event.getMentorId(), notification);
    }
}
