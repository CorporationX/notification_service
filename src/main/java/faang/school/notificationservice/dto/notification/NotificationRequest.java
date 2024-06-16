package faang.school.notificationservice.dto.notification;

import faang.school.notificationservice.dto.UserDto;
import lombok.Data;

@Data
public class NotificationRequest {
    private UserDto user;
    private String message;
}
