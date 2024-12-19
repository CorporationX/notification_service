package faang.school.notificationservice.listener.mentorshipoffered;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.notificationservice.event.MentorshipOfferedEvent;
import faang.school.notificationservice.listener.AbstractEventListener;
import faang.school.notificationservice.service.EventService;
import faang.school.notificationservice.service.NotificationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Service;


@Service
public class MentorshipOfferedEventListener extends AbstractEventListener<MentorshipOfferedEvent> {
    private final EventService eventService;

    public MentorshipOfferedEventListener(ObjectMapper objectMapper, EventService eventService) {
        super(objectMapper);
        this.eventService = eventService;
    }

    @Override
    public void onMessage(@NotNull Message message, byte[] pattern) {
        handleEvent(message, MentorshipOfferedEvent.class, event
                -> eventService.mentorshipOffered(event.idRequest(), event.idAuthor(), event.idRequester()));
    }
}
