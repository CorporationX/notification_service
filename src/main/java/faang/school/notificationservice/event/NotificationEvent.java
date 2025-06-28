package faang.school.notificationservice.event;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationEvent {

    private UserDto userDto;
    private NotificationEventType type;
}