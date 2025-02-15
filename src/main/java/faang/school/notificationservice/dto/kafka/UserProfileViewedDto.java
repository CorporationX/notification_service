package faang.school.notificationservice.dto.kafka;

public record UserProfileViewedDto(
        Long viewerId,
        Long profileOwnerId
) {}
