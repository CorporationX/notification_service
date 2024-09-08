package faang.school.notificationservice.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Long telegramId;
    private PreferredContact contactPreference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
