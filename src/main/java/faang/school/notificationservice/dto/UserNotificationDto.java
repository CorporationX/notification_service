package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class UserNotificationDto {
    private PreferredContact preference;
    private String email;
    private String phone;
    private Long chatId;
}
