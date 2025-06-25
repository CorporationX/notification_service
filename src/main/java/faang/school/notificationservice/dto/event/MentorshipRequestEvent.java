package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MentorshipRequestEvent {
    private Long followerId;
    private Long foloweeId;
    private long id;
}
