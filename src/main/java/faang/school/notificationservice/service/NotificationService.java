package faang.school.notificationservice.service;

import faang.school.notificationservice.dto.user.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public interface NotificationService {
    void send(
            @NotNull UserDto user,
            @NotBlank String message
    );
    UserDto.PreferredContact getPreferredContact();
}
