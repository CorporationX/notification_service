package faang.school.notificationservice.event.recommendationReceived;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationReceivedEvent {
    private long authorId;
    private long receivedId;
    private long recommendationId;
}