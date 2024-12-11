package faang.school.notificationservice.event.follower;

import lombok.Data;

@Data
public class UserFollowerEvent {
    private long followerId;
    private long followeeId;
}
