package faang.school.notificationservice.dto;

import faang.school.notificationservice.properties.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorshipAcceptedEvent {
    private long requestId;
    private long requesterId;
    private long receiverId;
    private EventType eventType;
}
