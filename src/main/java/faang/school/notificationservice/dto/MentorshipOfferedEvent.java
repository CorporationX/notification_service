package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class MentorshipOfferedEvent {
    private long authorId;
    private long mentorId;
}
