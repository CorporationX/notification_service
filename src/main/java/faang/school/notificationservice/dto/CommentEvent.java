package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEvent {
    @NotNull
    private Long authorOfCommentId;
    @Positive
    private Long authorOfPostId;
    @Positive
    private Long postId;
    private String content;
    @Positive
    private Long commentId;
    private LocalDateTime createdAt;
}