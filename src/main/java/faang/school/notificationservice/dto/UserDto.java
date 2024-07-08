package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private long telegramChatId;
    private PreferredContact preference;
    private String preferredLocale;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
