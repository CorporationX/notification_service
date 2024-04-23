package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact contactPreference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM, PHONE
    }
}
