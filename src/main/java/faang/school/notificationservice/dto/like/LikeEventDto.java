package faang.school.notificationservice.dto.like;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEventDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long authorPostId;

    @NotNull
    private Long postId;

    private LocalDateTime createdAt;
}