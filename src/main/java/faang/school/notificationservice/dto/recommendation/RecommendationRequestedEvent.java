package faang.school.notificationservice.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestedEvent {
    private Long id;
    private Long requesterId;
    private Long receiverId;
    private LocalDateTime createdAt;
}
