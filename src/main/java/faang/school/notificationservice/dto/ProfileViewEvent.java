package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProfileViewEvent {
    private long viewerId;
    private long profileOwnerId;
    private LocalDateTime timestamp;
}
