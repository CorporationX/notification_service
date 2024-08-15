package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String city;
    private String email;
    private String phone;
    private PreferredContact preference;
    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
