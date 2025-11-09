package faang.school.notificationservice.dto;

import lombok.Builder;

@Builder
public record CommentEventDto(
        Long commentId,
        Long postId,
        Long commentAuthorId,
        Long postAuthorId,
        String commentText
) {}
