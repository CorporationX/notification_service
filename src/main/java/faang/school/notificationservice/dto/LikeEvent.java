package faang.school.notificationservice.dto;

public record LikeEvent(Long authorId, Long likerId, Long postId) {
}
