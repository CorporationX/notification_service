package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    @NotNull(message = "User must have an id to be notification receiver.")
    private long id;
    private String username;
    @Email(message = "User's email has incorrect format.")
    @NotNull(message = "User must have an email.")
    private String email;
    @NotNull(message = "User must have phone number.")
    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
