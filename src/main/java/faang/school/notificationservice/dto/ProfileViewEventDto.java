package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileViewEventDto {
    private long authorId;
    private long viewerId;
    private LocalDateTime viewTime;
}
