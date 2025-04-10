package faang.school.notificationservice.dto;

import lombok.Data;

import java.util.Locale;

/**
 * DTO для представления информации о пользователе.
 * Содержит поля для идентификатора, имени пользователя, электронной почты,
 * номера телефона, предпочтительного способа связи и локали.
 */
@Data
public class UserDto {
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