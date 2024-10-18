package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class AchievementEvent {
    private long id;
    private String title;
    private String description;
    private String rarity;
    private long points;
    private long userId;
}
