package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
public record FollowerEventDto (
    @NotBlank
    long followerId,
    @NotBlank
    long followeeId,
    @NotBlank
    LocalDateTime timestamp) {
}
