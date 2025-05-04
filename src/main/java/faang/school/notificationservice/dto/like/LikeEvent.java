package faang.school.notificationservice.dto.like;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEvent {

    @NotNull(message = "Author of the post must not be null")
    private Long authorPostId;

    @NotNull(message = "Author of the like must not be null")
    private Long authorLikeId;

    @NotNull(message = "Post ID must not be null")
    private Long postId;

    private boolean isDeleted;

    @NotNull(message = "Creation time must not be null")
    @PastOrPresent(message = "Creation time must be in the past or present")
    private LocalDateTime createdAt;
}
