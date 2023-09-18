package faang.school.notificationservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private PreferredContact preference;
    @Builder.Default
    private Locale locale = Locale.UK;

    public enum PreferredContact {
        EMAIL, SMS, TELEGRAM
    }
}
