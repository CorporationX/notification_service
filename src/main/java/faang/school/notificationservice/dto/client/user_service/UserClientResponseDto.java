package faang.school.notificationservice.dto.client.user_service;

import lombok.Data;

import java.util.Locale;

@Data
public class UserClientResponseDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Locale locale;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
