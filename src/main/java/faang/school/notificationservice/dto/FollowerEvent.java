package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowerEvent {
    private long followerId;
    private long followeeId;
    private String eventType;
    private LocalDateTime eventTime;
}
