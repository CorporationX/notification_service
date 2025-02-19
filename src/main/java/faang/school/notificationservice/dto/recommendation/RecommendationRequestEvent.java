package faang.school.notificationservice.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestEvent {

    long authorId;

    long receiverId;

    long requestId;
}
