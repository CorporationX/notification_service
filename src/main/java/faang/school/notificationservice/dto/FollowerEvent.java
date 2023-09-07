package faang.school.notificationservice.dto;


import faang.school.notificationservice.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class FollowerEvent {
    private long followerId;
    private long followeeId;
    private EventType eventType;
    private Date receivedAt;
}