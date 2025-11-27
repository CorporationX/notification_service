package faang.school.notificationservice.dto.like;

import java.time.LocalDateTime;

public record UnlikeEventDto(
        Long likeId,
        Long postAuthorId,
        Long likeAuthorId,
        Long postId,
        LocalDateTime createdAt
) {}
