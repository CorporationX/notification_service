package faang.school.notificationservice.dto.mentorshiprequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedDto {

    private Long id;
    private Long receiverId;
    private Long requesterId;
    private String receiverUsername;
}
