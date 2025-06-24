package faang.school.notificationservice.exception;

import faang.school.notificationservice.dto.UserDto;

public class NotificationServiceNotFoundException extends RuntimeException {
    public NotificationServiceNotFoundException(UserDto.PreferredContact preferredContact) {
        super(String.format("Notification service not found for preferred contact: %s", preferredContact));
    }
}
