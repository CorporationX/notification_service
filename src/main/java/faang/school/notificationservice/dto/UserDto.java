package faang.school.notificationservice.dto;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
