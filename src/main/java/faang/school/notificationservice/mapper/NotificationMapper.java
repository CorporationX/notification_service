package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.StockAlertDto;
import faang.school.notificationservice.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "preference", ignore = true)
    UserDto toUserDto(StockAlertDto stockAlertDto);
}
