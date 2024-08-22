package faang.school.notificationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AchievementEvent {

    private Long id;
    private String title;
    private Long receiverId;
    private long points;
    private LocalDateTime receivingTime;
}