package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvent {
    @NotNull
    private long authorId;
    @NotNull
    private long postId;
    @NotNull
    private long commentId;
    @NotNull
    private long postAuthorId;
    @NotNull
    private String comment;
}
