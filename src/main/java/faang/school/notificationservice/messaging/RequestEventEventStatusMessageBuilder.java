package faang.school.notificationservice.messaging;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.RequestEventEvent;

import java.util.Locale;

public interface RequestEventEventStatusMessageBuilder {
    boolean isApplicable(RequestEventEvent event);

    String buildMessage(RequestEventEvent event, UserDto userDto, Locale locale);
}
