package faang.school.notificationservice.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementEventDto implements EventDto {

    @JsonIgnore
    private final EventType eventType = EventType.ACHIEVEMENT_EVENT;
    private Long userId;
    private String achievementTitle;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }
}
