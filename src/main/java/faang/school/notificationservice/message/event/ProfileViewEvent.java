package faang.school.notificationservice.message.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewEvent extends NotificationEvent {
    private long viewerUserId;
}