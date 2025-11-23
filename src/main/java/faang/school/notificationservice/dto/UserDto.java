package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String aboutMe;
    private PreferredContact contactPreference;
    private List<ContactDto> contacts;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}