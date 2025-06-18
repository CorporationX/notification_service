package faang.school.notificationservice.dto.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeEventDto {
    private Long postAuthorId;
    private Long likerId;
    private Long postId;
}
