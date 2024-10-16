package faang.school.notificationservice.dto;

public record SendingDto(
        UserDto userDto,
        String message
) {
}
