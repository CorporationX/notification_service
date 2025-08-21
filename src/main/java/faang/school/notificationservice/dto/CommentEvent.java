package faang.school.notificationservice.dto;

public record CommentEvent(
        Long postId,
        Long postAuthorId,
        Long authorId,
        Long commentId,
        String content
) {
}
