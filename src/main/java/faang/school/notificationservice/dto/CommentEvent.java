package faang.school.notificationservice.dto;

public record CommentEvent(
        Long commentId,
        Long postAuthorId,
        Long commentAuthorId,
        Long postId,
        String text
) {
}