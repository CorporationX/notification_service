package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FollowerEvent {
    private long id;
    private long follower;
    private long followee;
    private LocalDateTime followedAt;
}
