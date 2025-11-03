package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SendSmsRequestDto(
        @NotNull
        long userId,
        @NotNull
        String phone,
        @NotEmpty
        String message
) {

}
