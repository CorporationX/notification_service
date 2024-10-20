package faang.school.notificationservice.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfileViewEventDto {
    private long receiverId;
    private String receiverName;
    private long actorId;
    private String actorName;
    private LocalDateTime receivedAt;
}
