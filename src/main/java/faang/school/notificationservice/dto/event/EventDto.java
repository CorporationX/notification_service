package faang.school.notificationservice.dto.event;

import faang.school.notificationservice.dto.user.UserDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String location;
    private UserDto userDto;
}
