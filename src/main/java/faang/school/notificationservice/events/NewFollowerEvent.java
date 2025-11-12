package faang.school.notificationservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain event representing when one user starts following another.
 * Published to Kafka and consumed by notification-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewFollowerEvent {

    /** ID of the user who followed someone */
    private long followerId;

    /** ID of the user who was followed */
    private long targetUserId;

    /** Display name of the follower, used in messages */
    private String followerDisplayName;
}