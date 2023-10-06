package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MentorshipAcceptedEventDto {
    private long id;
    private long requesterId;
    private long receiverId;
}
