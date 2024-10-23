package faang.school.notificationservice.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationReceivedEvent {
    private Long authorId;
    private Long receiverUserId;
    private Long recommendationId;
}
