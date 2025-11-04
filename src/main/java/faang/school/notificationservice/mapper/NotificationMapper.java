package faang.school.notificationservice.mapper;

import faang.school.notificationservice.dto.SendSmsRequestDto;
import faang.school.notificationservice.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "userId", target = "id")
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "preference", constant = "PHONE")
    UserDto toUserDto(SendSmsRequestDto userDto);
}