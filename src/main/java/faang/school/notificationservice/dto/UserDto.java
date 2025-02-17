package faang.school.notificationservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private String telegramChatId;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
