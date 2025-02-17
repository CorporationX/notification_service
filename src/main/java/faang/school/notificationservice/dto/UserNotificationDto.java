package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.Locale;

@Data
public class UserNotificationDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Long chatId;
    private PreferredContact preference;
    private final Locale locale;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
