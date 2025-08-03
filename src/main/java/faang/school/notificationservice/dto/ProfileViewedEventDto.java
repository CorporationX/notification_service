package faang.school.notificationservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@ToString
public class ProfileViewedEventDto {
    private final String viewerName;
    private final String userName;
    private final Long viewerId;
    private final Long viewedId;
    private final LocalDateTime localDateTime;
}
