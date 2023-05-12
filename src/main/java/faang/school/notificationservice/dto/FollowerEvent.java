package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class FollowerEvent {
    private long followerId;
    private long followeeId;
}
