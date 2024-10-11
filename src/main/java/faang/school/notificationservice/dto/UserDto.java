package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Long telegramChatId;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
