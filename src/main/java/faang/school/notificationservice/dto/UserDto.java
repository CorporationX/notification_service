package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;

    public UserDto(long id, String phone, PreferredContact preference) {
        this.id = id;
        this.phone = phone;
        this.preference = preference;
    }

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
