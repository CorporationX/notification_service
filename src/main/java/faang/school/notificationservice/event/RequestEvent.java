package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestEvent extends Event {
    private UUID idempotencyKey;
    private Long userId;
    private String requestType;
    private String currentStatus;
    private String statusDetails;
    private String eventType;
    private LocalDateTime timestamp;
}
