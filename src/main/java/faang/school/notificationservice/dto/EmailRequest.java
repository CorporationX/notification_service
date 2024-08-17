package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private UserDto user;
    private String message;
}
