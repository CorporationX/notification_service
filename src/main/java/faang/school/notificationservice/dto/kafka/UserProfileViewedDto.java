package faang.school.notificationservice.dto.kafka;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserProfileViewedDto(
        @NotNull
        Long viewerId,
        @NotNull
        Long profileOwnerId,
        @NotNull
        LocalDateTime viewedTime
) {}
