package faang.school.notificationservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private List<ContactDto> contacts;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM, SMS
    }
}
