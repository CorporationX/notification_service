package faang.school.notificationservice.messaging.event;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
public class EventStartEvent implements Serializable {
    private String eventId;
    private List<UserDto> participants;
}
