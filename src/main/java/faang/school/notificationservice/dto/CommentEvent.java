package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    @NotNull
    private Long idAuthorComment;
    @NotNull
    private Long idAuthorPost;
    @NotNull
    private Long idPost;
    @NotBlank
    private String text;
    @NotNull
    private Long comment;
}
