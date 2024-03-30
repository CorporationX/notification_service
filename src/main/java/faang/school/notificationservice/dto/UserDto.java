package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    @JsonIgnore(value = false)
    private long telegramChatId;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
