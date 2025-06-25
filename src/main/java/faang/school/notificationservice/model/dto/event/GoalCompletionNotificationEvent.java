package faang.school.notificationservice.model.dto.event;

import faang.school.notificationservice.model.dto.UserDto;
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
