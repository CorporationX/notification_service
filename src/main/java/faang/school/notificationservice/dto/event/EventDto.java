package faang.school.notificationservice.dto.event;

import lombok.Getter;

@Getter
public abstract class EventDto {

    private EventType eventType;
    private Long userId;
}
