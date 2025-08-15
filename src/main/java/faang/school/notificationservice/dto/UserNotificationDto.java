package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNotificationDto {
    private PreferredContact preference;
    private String email;
    private String phone;
    private Long chatId;

    public enum PreferredContact {
        EMAIL, PHONE, TELEGRAM
    }
}
