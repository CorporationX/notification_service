package faang.school.notificationservice.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AchievementEvent {
    private long userId;
    private String title;
    private LocalDateTime timestamp;
}
