package faang.school.notificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestedEvent {
    private Long requesterId;
    private Long receiverId;
    private Long requestId;
}
