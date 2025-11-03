package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;

public record SendSmsRequestDto(
        long userId,
        String phone,
        String message
) {

}
