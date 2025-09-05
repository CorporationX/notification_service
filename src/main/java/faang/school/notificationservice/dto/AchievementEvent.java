package faang.school.notificationservice.dto;

public record AchievementEvent(
        Long postId,
        Long postAuthorId,
        Long authorId,
        Long commentId,
        String content
) {
}
