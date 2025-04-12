package faang.school.notificationservice.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class EventStartEvent implements Serializable {

    private String eventId;
    private List<String> participantIds;

}
