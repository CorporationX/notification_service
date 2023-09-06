package faang.school.notificationservice.dto.achievement;
import lombok.Data;

@Data
public class DtoAchievement {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
}
