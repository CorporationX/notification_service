package faang.school.notificationservice.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievementEvent {
    private long userId;
    private long achievementId;
    private String achievementTitle;
}
