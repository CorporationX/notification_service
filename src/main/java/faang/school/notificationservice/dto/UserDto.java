package faang.school.notificationservice.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;

    @Email
    private String email;

    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM, PHONE;
    }
}
