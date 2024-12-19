package faang.school.notificationservice.dto.redisevent;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AchievementEvent {
    private long userId;
    private String achievement;
    private LocalDateTime timeStamp;
}
