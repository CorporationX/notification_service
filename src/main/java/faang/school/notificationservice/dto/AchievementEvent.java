package faang.school.notificationservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class AchievementEvent {
    private long userId;
    private String achievement;
//    private long followeeId;
    private LocalDateTime timeStamp;
}
