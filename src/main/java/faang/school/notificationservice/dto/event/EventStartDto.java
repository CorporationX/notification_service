package faang.school.notificationservice.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventStartDto {
    private Long id;
    private List<Long> attendeeIds;
    private String title;
    private LocalDateTime startDate;
    private UserDto notifiedAttendee;
    private Long timeTillStart;
}
