package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistrationNotificationDto {
    private Long userId;
    private Long eventId;
    private String message;
    private String telegramId;
}
