package faang.school.notificationservice.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorshipRequestAcceptedDto {
    private long requestId;
    private long receiverId;
    private long actorId;
}
