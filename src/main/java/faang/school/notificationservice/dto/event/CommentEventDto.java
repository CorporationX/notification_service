package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEventDto {
    private Long commentId;
    private Long commenterId;
    private Long postId;
    private Long postAuthorId;
    private String text;
}