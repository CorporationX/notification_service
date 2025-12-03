package faang.school.notificationservice.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Locale;

public record RecommendationEventDto(
        @NotNull(message = "Author recommendation cannot be null")
        @Positive(message = "Author recommendation must have positive")
        Long authorId,
        @NotNull(message = "Received recommendation cannot be null")
        @Positive(message = "Received recommendation must have positive")
        Long receiverId,
        @NotNull(message = "Recommendation recommendation cannot be null")
        @Positive(message = "Received recommendation must have positive")
        Long recommendationId,
        @NotBlank(message = "Content cannot be empty")
        String content,
        Locale locale
) {
}
