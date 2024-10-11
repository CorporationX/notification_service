package faang.school.notificationservice.model.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String telegramUsername;
    private String telegramUserId;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
