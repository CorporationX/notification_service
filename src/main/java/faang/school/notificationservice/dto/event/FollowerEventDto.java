package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerEventDto {
    private long followerId;
    private long followeeId;
    private LocalDateTime subscriptionTime;
}
