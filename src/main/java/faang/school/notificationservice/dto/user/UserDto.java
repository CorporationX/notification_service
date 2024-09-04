package faang.school.notificationservice.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private String password;
    private boolean active;
    private String aboutMe;
    private String countryTitle;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
