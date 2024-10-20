package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementEvent implements Serializable {
    private long userId;
    private long achievementId;
    private String achievementTitle;
}