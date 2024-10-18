package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public UserDto(long id, String username, String email, String phone, PreferredContact preference) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.preference = preference;
    }
}
