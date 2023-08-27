package faang.school.notificationservice.dto.event;

import lombok.Getter;


public interface EventDto {

    long getUserId();

    EventType getEventType();
}
