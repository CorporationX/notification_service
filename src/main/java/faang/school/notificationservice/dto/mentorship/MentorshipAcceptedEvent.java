package faang.school.notificationservice.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MentorshipAcceptedEvent {
    private long id;
    private long authorId;
    private long receiverId;
    private String content;
    private LocalDateTime acceptedAt;
}
