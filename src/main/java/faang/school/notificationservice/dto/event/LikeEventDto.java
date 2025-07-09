package faang.school.notificationservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeEventDto {
    private Long authorId;
    private Long senderId;
    private String senderName;
    private Long postId;
    private Long commentId;
    private LocalDateTime date;
}
