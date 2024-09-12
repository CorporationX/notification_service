package faang.school.notificationservice.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Builder;

@Builder(toBuilder = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Long telegramId;
    private PreferredContact contactPreference;
    private PreferredContact preference;
    private String password;
    private boolean active;
    private String aboutMe;
    private String countryTitle;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }

}
