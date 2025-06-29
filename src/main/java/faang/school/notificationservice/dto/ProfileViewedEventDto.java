package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewedEventDto {
    private String viewerName;
    private Long viewerId;
    private Long viewedId;
    private LocalDateTime localDateTime;
}
