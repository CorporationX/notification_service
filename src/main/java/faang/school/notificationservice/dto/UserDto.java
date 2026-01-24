package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    @JsonProperty("preferredContact")
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
