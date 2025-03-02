package faang.school.notificationservice.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileViewEvent {
    private long receiverId;
    private long actorId;
    private LocalDateTime receivedAt;
}