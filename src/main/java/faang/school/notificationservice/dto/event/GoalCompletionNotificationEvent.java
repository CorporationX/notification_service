package faang.school.notificationservice.dto.event;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompletionNotificationEvent implements NotificationEvent {
    UserDto userDto;
    String goalTitle;
}
