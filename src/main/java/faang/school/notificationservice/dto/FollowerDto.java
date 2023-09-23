package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowerDto {
    private Long followerId;
    private Long followingId;
    private LocalDateTime followDate;
}
