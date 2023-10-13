package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentorshipAcceptedEventDto {
    private long id;
    private long requesterId;
    private long receiverId;
}
