package faang.school.notificationservice.event.mentorship.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipOfferedEvent {
    private UUID eventId;
    private long mentorshipOfferId;
    private long requesterId;
    private long receiverId;
}
