package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.Locale;

/**
 * DTO для пользователя.
 * Содержит идентификатор, имя пользователя, email, телефон и предпочтительный способ связи.
 */
@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private String language;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}