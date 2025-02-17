package faang.school.notificationservice;

public record LikeEvent(Long authorId, Long likerId, Long postId) {
}
