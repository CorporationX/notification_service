package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String aboutMe;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
