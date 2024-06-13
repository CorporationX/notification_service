package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ContactPreferenceDto {

    private long id;

    @NotNull(message = "UserId should not be null")
    @Positive(message = "UserId should be positive")
    private Long userId;

    @NotNull(message = "Type should not be null")
    private UserDto.PreferredContact preference;
}
