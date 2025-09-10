package faang.school.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationDto {
    private PreferredContact preferredContact;
    private String email;
    private String phone;
    private Long chatId;
}
