package faang.school.notificationservice.event.kafka;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewProfileEvent implements NotificationEvent {
    private UserDto owner;
    private UserDto follower;
}
