package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.Locale;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private Locale locale;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
