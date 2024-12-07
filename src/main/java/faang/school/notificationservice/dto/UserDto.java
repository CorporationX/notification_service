package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @PositiveOrZero(message = "Id must be positive or zero")
    @NotNull(message = "Id is required")
    private long id;

    @Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters")
    private String username;

    @Email
    private String email;

    @Pattern(
            regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",
            message = "Phone number must be valid and include country code if needed"
    )
    private String phone;

    @NotNull(message = "Preferred contact is required")
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
