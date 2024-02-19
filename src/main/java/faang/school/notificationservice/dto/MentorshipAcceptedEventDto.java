package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedEventDto {
    private long id;
    private long requesterId;
    private long receiverId;
    private String requesterName;
    private String receiverName;
}