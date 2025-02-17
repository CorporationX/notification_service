package faang.school.notificationservice.dto.kafka;

import java.time.LocalDateTime;

public record UserProfileViewedDto(
        Long viewerId,
        Long profileOwnerId,
        LocalDateTime viewedTime
) {}
