package faang.school.notificationservice.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedEvent {
    private long id;
    private long authorId;
    private long receiverId;
    private LocalDateTime acceptedAt;
}
