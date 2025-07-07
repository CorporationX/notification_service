package faang.school.notificationservice.event.kafka;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventStartNotificationEvent implements NotificationEvent {
    private UserDto owner;
    private List<UserDto> attendees;
    private String eventTitle;
}
