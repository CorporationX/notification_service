package faang.school.notificationservice.dto;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String chatId;
    private String locale;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }

    public boolean hasChatId() {
        return chatId != null;
    }
}
