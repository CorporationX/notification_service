package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerEvent {

    private Long visitorId;
    private Long visitedId;
    private Long projectId;
    private LocalDateTime subscribedDateTime;
}
