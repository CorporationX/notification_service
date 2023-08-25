package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProfileViewEvent {
    private Long userId;
    private Long profileViewedId;
    private LocalDateTime date;
}
