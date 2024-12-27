package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementEvent {
    private String userName;
    private Long userId;
    private String achievementTitle;
    private Long achievementId;
}