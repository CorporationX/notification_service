package faang.school.notificationservice.redis.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRedisEvent {
    Long userId;
    String achievementName;
}
