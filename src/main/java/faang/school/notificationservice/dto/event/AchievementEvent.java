package faang.school.notificationservice.dto.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class AchievementEvent extends ApplicationEvent {
    private Long userId;
    private String achievement;

    public AchievementEvent(Object source, long userId, String achievement) {
        super(source);
        this.userId = userId;
        this.achievement = achievement;
    }
}
