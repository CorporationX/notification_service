package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedEvent {
    private long requesterUserId;
    private long receiverUserId;
    private LocalDateTime timestamp;
}
