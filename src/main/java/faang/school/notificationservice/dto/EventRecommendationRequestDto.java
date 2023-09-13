package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventRecommendationRequestDto {
    private Long requesterId;
    private Long receiverId;
    private Long recommendationId;
}
