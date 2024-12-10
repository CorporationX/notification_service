package faang.school.notificationservice.event;

import lombok.Data;

@Data
public class UserFollowerEvent {
    private long followerId;
    private long followeeId;
}
