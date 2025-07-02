package faang.school.notificationservice.mapper.kafka;

import faang.school.notificationservice.dto.client.user_service.EventDto;
import faang.school.notificationservice.model.kafka.event.EventMessage;

public class KafkaMapper {

    public static EventMessage toEventMessage(EventDto dto, String initiatorName) {
        return new EventMessage(
                dto.getTitle(),
                dto.getDescription(),
                dto.getStartDate().toString(),
                dto.getEndDate().toString(),
                dto.getLocation(),
                dto.getType().getMessage(),
                dto.getStatus().getMessage(),
                initiatorName
        );
    }
}
