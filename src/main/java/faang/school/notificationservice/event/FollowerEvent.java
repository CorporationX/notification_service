package faang.school.notificationservice.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FollowerEvent(
        @NotBlank
        Long followerId,
        @NotBlank
        Long followeeId,
        @NotBlank
        LocalDateTime timestamp) {
}
