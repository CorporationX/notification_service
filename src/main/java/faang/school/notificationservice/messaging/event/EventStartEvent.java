package faang.school.notificationservice.messaging.event;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class EventStartEvent {

    private String eventId;
    private List<UserDto> participants;
}
