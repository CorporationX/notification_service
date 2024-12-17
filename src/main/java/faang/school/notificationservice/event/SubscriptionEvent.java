package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionEvent {
    private Long followerId;
    private Long followeeId;
    private LocalDateTime subscribedAt;
    private String followerName;
    private String followeeName;
}