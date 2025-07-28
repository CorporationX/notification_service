package faang.school.notificationservice.event;

import faang.school.notificationservice.dto.UserDto;

public interface NotificationEvent {

    UserDto owner();
}