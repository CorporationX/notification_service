package faang.school.notificationservice.dto;

import faang.school.notificationservice.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowerEvent implements Serializable {
    private EventType eventType;
    private Date receivedAt;
    private Long followerId;
    private Long followeeId;
}
