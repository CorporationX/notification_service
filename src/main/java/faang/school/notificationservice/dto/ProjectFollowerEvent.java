package faang.school.notificationservice.dto;

import lombok.Data;

@Data
public class ProjectFollowerEvent {
    private Long projectId;
    private Long followerId;
    private Long authorId;
}
