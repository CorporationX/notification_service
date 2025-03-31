package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@NotNull
@Builder
public class LikeEvent {
    @NotNull
    @PositiveOrZero
    private Long postId;

    @NotNull
    @PositiveOrZero
    private Long authorId;

    @NotNull
    private LocalDateTime likeTime;

    @NotNull
    @PositiveOrZero
    private Long userId;

}