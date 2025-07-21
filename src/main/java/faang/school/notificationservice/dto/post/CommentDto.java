package faang.school.notificationservice.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CommentDto {
    private Long commentId;
    private Long postId;
    @NotNull
    private Long authorId;
    private String content;
    private String createData;
    private String updateData;
}
