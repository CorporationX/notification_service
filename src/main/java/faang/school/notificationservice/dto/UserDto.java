package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private Locale locale;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}