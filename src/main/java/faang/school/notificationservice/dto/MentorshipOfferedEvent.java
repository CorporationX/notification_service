package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MentorshipOfferedEvent {
    private long authorId;
    private long mentorId;
    private LocalDateTime receivedAt;
}
