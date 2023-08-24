package faang.school.notificationservice.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import faang.school.notificationservice.dto.SkillDto;
import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long ownerId;
    private String description;
    private List<SkillDto> relatedSkills;
    private String location;
    private int maxAttendees;
    @JsonIgnore
    private UserDto attendee;
    @JsonIgnore
    private long timeTillStart;
}
