package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorshipOfferedEvent {

    private Long id;
    private Long requesterId;
    private Long receiverId;
}
