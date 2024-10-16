package faang.school.notificationservice.dto;

import lombok.Data;


public record SendingDto (
    UserDto userDto,
    String message
) {}
