package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FollowerDto {
    private Long followerId;
    private Long followeeId;
    private LocalDateTime followDate;
}
