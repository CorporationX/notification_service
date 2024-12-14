package faang.school.notificationservice.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecommendationReceivedEvent {
    private long id;
    private long authorId;
    private long receiverId;
    private String content;
    private LocalDateTime recommendationTime;
}
