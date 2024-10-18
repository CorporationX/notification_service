package faang.school.notificationservice.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MentorshipAcceptedEvent {
    Long requestId;
    Long requesterId;
    Long mentorId;
}
