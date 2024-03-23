package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private Locale locale;
    private PreferredContact preference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
