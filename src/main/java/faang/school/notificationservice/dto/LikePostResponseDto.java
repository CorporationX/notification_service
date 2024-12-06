package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikePostResponseDto {

    @NotNull
    private Long authorPostId;

    @NotNull
    private Long likedUserId;

    @NotNull
    private Long postId;

    @NotNull
    private LocalDateTime likeTime;

}
