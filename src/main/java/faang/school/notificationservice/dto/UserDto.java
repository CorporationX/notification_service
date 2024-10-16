package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String telegram;
    @JsonSetter("preferredContact")
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
