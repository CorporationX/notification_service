package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Long telegramChatId;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
