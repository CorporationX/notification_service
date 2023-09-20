package faang.school.notificationservice.dto.redis;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserAchievementEvent {
    private long id;
    private Long achievementId;
    private long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
