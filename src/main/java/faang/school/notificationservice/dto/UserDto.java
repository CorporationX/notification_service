package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private long telegramId;
    private PreferredContact preference;
    private boolean active;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
