package faang.school.notificationservice.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PostDto {
    private Long postId;
    private String content;
    @NotNull
    private Long authorId;
    private Long projectId;
    private LocalDateTime scheduledAt;
}