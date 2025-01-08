package faang.school.notificationservice.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerEvent {
    private Long followerId;
    private Long followeeId;
}
