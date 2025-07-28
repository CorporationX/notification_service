package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.notification.PendingNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;

import java.util.List;
import java.util.stream.Collectors;

public class PendingNotificationsMapper {

    public static PendingNotificationsDto toDto(PendingNotifications entity) {
        return PendingNotificationsDto.builder()
                .receiverId(entity.getReceiverId())
                .targetEntityId(entity.getTargetEntityId())
                .relatedEntityId(entity.getRelatedEntityId())
                .eventType(entity.getEventType())
                .status(entity.getStatus())
                .build();
    }

    public static PendingNotifications toEntity(PendingNotificationsDto dto) {
        return PendingNotifications.builder()
                .receiverId(dto.getReceiverId())
                .targetEntityId(dto.getTargetEntityId())
                .relatedEntityId(dto.getRelatedEntityId())
                .eventType(dto.getEventType())
                .status(dto.getStatus())
                .build();
    }

    public static List<PendingNotificationsDto> toDtoList(List<PendingNotifications> entities) {
        return entities.stream()
                .map(PendingNotificationsMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<PendingNotifications> toEntityList(List<PendingNotificationsDto> dtos) {
        return dtos.stream()
                .map(PendingNotificationsMapper::toEntity)
                .collect(Collectors.toList());
    }
}