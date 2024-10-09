package faang.school.notificationservice.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProfileViewEventDto {
    private Long sender_id;
    private Long receiver_id;
    private LocalDateTime dateTime;
}
