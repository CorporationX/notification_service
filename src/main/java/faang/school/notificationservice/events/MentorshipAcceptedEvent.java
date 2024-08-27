package faang.school.notificationservice.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedEvent {

    private long requesterId;

    private long receiverId;

    private long mentorshipRequestId;

    private LocalDateTime sendAt;
}
