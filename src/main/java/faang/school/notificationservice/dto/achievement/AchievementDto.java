package faang.school.notificationservice.dto.achievement;
import lombok.Data;

@Data
public class AchievementDto {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
}
