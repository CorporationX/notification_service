package faang.school.notificationservice.dto.messagebroker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MentorshipAcceptedEvent {
    private long requestAuthorId;
    private long idRequestRecipient;
    private long requestId;
}