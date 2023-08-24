package faang.school.notificationservice.dto;

import faang.school.notificationservice.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent implements Serializable {
    private EventType eventType;
    private LocalDateTime receivedAt;
    private Long followerId;
    private Long followeeId;
}
