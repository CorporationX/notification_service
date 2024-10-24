package faang.school.notificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFollowerEvent {
    private Long followerId;
    private Long projectId;
    private Long creatorId;
}