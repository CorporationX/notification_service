package faang.school.notificationservice.dto;

public record LikePostEvent(Long authorId, Long likerId, Long postId) {
}
