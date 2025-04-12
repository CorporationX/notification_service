package faang.school.notificationservice.messaging.event;

import faang.school.notificationservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class EventStartEvent {

    private String eventId;  // ID события
    private List<UserDto> participants;  // Список участников с полными данными (UserDto)

    // Если метод getParticipants() не был добавлен, то можно добавить вручную:
    public List<UserDto> getParticipants() {
        return participants;
    }
}
