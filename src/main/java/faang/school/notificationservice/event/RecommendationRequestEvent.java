package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequestEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long requesterId;
    private Long receiverId;
    private Long recommendationRequestId;
}