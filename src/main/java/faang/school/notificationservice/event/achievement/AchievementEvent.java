package faang.school.notificationservice.event.achievement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementEvent {
    private Long userId;
    private String title;
    private String description;
}
