package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String email;

    @NotBlank(message = "To send SMS, a phone number is a mandatory requirement")
    private String phone;
    private String telegramChatId;
    private PreferredContact preference;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
