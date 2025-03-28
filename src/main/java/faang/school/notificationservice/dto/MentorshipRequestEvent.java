package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class MentorshipRequestEvent {
    private long requesterId;
    private long mentorId;
    private long requestId;
}
