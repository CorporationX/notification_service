package faang.school.notificationservice.dto.redis;

import lombok.Data;

@Data
public class RecommendationRequestEvent {
    private Long id;
    private Long requesterId;
    private Long receiverId;
}
