package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.Locale;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Locale locale;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
