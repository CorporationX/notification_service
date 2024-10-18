package faang.school.notificationservice.dto.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowerEvent {
    private Long followerId;
    private Long followeeId;
    private LocalDateTime eventTime;
}
