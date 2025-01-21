package faang.school.notificationservice.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationReceivedEvent {
    private long id;
    private long authorId;
    private long receiverId;
    private String content;
    private LocalDateTime recommendationTime;
}
