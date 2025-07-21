package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.notification.PendingNotificationsDto;
import faang.school.notificationservice.model.PendingNotifications;

import java.util.List;
import java.util.stream.Collectors;

public class PendingNotificationsMapper {

    public static PendingNotificationsDto toDto(PendingNotifications entity) {
        return new PendingNotificationsDto(
                entity.getReceiverId(),
                entity.getTargetEntityId(),
                entity.getRelatedEntityId(),
                entity.getEventType(),
                entity.getStatus()
        );
    }

    public static PendingNotifications toEntity(PendingNotificationsDto dto) {
        PendingNotifications entity = new PendingNotifications();
        entity.setReceiverId(dto.getReceiverId());
        entity.setTargetEntityId(dto.getTargetEntityId());
        entity.setRelatedEntityId(dto.getRelatedEntityId());
        entity.setEventType(dto.getEventType());
        entity.setStatus(dto.getStatus());
        return entity;
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