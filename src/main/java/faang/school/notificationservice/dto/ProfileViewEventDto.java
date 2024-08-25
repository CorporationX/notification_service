package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileViewEventDto {
    private long profileId;
    private long viewerId;
    private LocalDateTime viewTime;
}
