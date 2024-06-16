package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNotificationDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
