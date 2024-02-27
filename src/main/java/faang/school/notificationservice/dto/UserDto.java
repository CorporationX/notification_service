package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
