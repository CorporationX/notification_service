package faang.school.notificationservice.dto.redisevent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementEvent {
    private long userId;
    private String achievement;
}
