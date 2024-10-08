package faang.school.notificationservice.event;

import lombok.Builder;

@Builder
public record LikeEventDto(long postAuthorId,
                           long likeAuthorId,
                           long postId) {
}
