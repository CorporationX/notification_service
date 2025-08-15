package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class UserNotificationDto {
    private PreferredContact preference;
    private String email;
    private String phone;
    private Long chatId;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
