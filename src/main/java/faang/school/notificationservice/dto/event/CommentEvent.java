package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent implements Serializable {
    private Long commentId;
    private Long commentAuthorId;
    private Long postAuthorId;
    private Long postId;
    private String commentText;
    private LocalDateTime createdAt;
}

