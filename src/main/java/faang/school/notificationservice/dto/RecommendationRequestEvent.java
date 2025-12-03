package faang.school.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecommendationRequestEvent(
        @NotNull
        Long id,
        @NotNull
        Long requesterId,
        @NotNull
        Long recommenderId,
        @NotBlank
        String text
) {

}
