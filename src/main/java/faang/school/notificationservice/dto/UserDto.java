package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private String message;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
