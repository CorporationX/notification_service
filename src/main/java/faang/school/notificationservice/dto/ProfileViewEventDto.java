package faang.school.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileViewEventDto {
    private Long idVisitor;
    private Long idVisited;
    private ZonedDateTime dateTime;
}
