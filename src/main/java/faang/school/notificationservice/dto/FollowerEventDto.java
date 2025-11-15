package faang.school.notificationservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Locale;

@Data
@Getter
@Builder
public class FollowerEventDto {
    private long followerId;
    private long followeeId;
    private LocalDateTime followTime;
    private Locale locale;

}
