package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedEvent {
    private Long mentorshipRequestId;
    private String description;
    private Long receiverId;
    private String receiverUserName;
    private Long requesterId;
    private String requesterUserName;
}
