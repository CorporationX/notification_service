package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestedEvent implements Serializable {
    private Long senderId;
    private Long receiverId;
    private Long recommendationId;
}