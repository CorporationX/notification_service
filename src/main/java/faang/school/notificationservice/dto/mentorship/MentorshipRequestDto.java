package faang.school.notificationservice.dto.mentorship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MentorshipRequestDto {
    private long requesterId;
    private long receiverId;
    private LocalDateTime createdAt;
}
