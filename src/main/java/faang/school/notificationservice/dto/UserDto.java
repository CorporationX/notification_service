package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private boolean active;
    private long countryId;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
