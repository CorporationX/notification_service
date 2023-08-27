package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MentorshipEventDto {
    private Long requesterId;
    private Long receiverId;
    private LocalDateTime createdAt;
}
