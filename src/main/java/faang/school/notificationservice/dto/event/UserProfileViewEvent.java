package faang.school.notificationservice.dto.event;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserProfileViewEvent(
        @NotBlank
        Long visitedUserId,
        @NotBlank
        Long visitorUserId,
        @NotBlank
        LocalDateTime dateTime) {
}