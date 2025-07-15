package faang.school.notificationservice.event.kafka;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreationNotificationEvent implements NotificationEvent {
    private String shortContent;
    private UserDto owner;
    private String commentAuthorUserName;
}
