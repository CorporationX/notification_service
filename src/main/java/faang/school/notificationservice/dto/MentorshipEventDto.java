package faang.school.notificationservice.dto;

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
