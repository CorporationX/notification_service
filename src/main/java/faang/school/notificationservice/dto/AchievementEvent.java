package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AchievementEvent {
    Long id;
    Long achievementId;
    Long userId;
    LocalDateTime achievementDttm;
}
