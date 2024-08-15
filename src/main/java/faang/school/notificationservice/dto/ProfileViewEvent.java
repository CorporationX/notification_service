package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileViewEvent {
    private long authorId;
    private long viewerId;
    private LocalDateTime viewTime;
}
