package faang.school.notificationservice.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowerEvent {
    private long followerId;
    private long followeeId;
    private LocalDateTime eventTime;
}
