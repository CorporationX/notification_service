package faang.school.notificationservice.dto;

import lombok.Builder;

@Builder
public record CommentEventDto(
        Long commentId,
        Long postId,
        Long postAuthorId,
        String commentText,
        String commentAuthorName,
        String postContent
) {}
