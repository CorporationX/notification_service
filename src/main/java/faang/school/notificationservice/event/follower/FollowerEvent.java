package faang.school.notificationservice.event.follower;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent {
    private long followerId;
    private long followeeId;
}