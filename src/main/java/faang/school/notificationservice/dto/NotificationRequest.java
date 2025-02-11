package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotNull
    private UserDto userDto;
    @NotBlank
    private String message;
}
