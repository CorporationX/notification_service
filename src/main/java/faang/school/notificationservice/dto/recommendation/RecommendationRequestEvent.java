package faang.school.notificationservice.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestEvent {

    private long authorId;

    private long receiverId;

    private long requestId;
}
