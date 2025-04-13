package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private UserDto user;
    private String message;
}
