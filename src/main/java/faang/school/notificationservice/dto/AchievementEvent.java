package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class AchievementEvent {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long userId;

    public enum Rarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY
    }
}
