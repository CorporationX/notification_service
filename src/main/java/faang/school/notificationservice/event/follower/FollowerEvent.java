package faang.school.notificationservice.event.follower;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent {

    private Long followerId;
    private Long followeeId;
    private Long projectId;
    private LocalDateTime date;
}
