package faang.school.notificationservice.dto;

import java.util.List;

public record EventStartEventDto(
        Long eventId,
        Long userId,
        String nameOwner,
        List<UserDto> attendeesUser,
        String titleEvent,
        TimeLeft timeLeft
) {
}
