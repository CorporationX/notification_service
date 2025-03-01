package faang.school.notificationservice.event;

import lombok.Data;

@Data
public class ProfileViewEvent {
    long viewerId;
    long userId;
}
