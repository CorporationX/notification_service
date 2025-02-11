package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentEvent {
    private final String content;
    private final Long authorId;
    private final LocalDateTime createdAt;
}
