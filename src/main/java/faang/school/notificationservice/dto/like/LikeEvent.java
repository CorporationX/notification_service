package faang.school.notificationservice.dto.like;

public record LikeEvent (Long postUserId, Long likeUserId, Long postId){}
