package faang.school.notificationservice.dto;

import faang.school.notificationservice.model.EventType;
import lombok.Data;

import java.util.List;

@Data
public class EventStartEvent {

    private Long id;
    private String title;
    private EventType eventType;
    private List<Long> attendeesIds;
}