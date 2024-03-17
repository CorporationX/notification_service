package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipAcceptedEventDto {
    private long requesterId;
    private long receiverId;
    private LocalDateTime createdAt;
    private String requesterName;
    private String receiverName;
}