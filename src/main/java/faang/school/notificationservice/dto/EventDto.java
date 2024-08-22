package faang.school.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String title;
    private LocalDateTime startDate;
}
