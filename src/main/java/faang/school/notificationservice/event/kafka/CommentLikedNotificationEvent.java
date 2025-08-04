package faang.school.notificationservice.event.kafka;

import faang.school.notificationservice.dto.UserDto;
import faang.school.notificationservice.event.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikedNotificationEvent implements NotificationEvent {
    private Long commentId;
    private String likerUsername;
    private UserDto owner;
    private String shortContent;
}