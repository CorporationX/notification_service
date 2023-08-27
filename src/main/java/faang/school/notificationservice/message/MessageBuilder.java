package faang.school.notificationservice.message;

import faang.school.notificationservice.dto.event.EventDto;
import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.dto.event.EventType;

public interface MessageBuilder {

    public String buildMessage(UserDto userDto, EventDto eventDto);

    public EventType getEventType();
}
