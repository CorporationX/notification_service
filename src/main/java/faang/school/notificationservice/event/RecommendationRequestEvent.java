package faang.school.notificationservice.event;

import faang.school.notificationservice.dto.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecommendationRequestEvent extends Event {
    private Long recommendationRequestId;
    private UserDto author;
    private UserDto receiver;
}
