package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NotNull
@RequiredArgsConstructor
@Builder
public class LikeEvent {
    @NotNull
    @PositiveOrZero
    private final Long postId;

    @NotNull
    @PositiveOrZero
    private final Long authorId;

    @NotNull
    private final LocalDateTime likeTime;

    @NotNull
    @PositiveOrZero
    private final Long userId;

}