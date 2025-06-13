package faang.school.notificationservice.exception;

import faang.school.notificationservice.dto.UserDto;

public class NotificationServiceNotFoundException extends RuntimeException {
    public NotificationServiceNotFoundException(UserDto.PreferredContact preferredContact) {
        super("Notification service not found for preferred contact: " + preferredContact);
    }
}
