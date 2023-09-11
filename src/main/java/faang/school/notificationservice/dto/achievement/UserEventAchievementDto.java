package faang.school.notificationservice.dto.achievement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEventAchievementDto {
    private long userId;
    private AchievementDto achievement;
}
