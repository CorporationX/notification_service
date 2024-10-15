package faang.school.notificationservice.model.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorshipAcceptedEvent {
    Long requestId;
    Long requesterId;
    Long mentorId;
}
