package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AchievementEvent {
    private Long id;
    private Long achievementId;
    private Long userId;
    private LocalDateTime achievementDttm;
}
