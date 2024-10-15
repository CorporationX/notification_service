package faang.school.notificationservice.model.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileViewEvent {
    private Long viewerId;
    private Long profileOwnerId;
}
